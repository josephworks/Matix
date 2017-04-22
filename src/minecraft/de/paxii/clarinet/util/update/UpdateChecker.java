package de.paxii.clarinet.util.update;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.threads.ThreadChain;
import de.paxii.clarinet.util.web.JsonFetcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.*;

import lombok.Getter;

public class UpdateChecker {
  @Getter
  private static VersionObject latestVersion;
  @Getter
  private static boolean upToDate = true;

  public UpdateChecker() {
    this.checkForUpdates();
  }

  private void checkForUpdates() {
    final ThreadChain threadChain = new ThreadChain();
    final File updater = new File(ClientSettings.getClientFolderPath().getValue(), "Updater.jar");

    threadChain.chainRunnable(() -> {
      if (updater.exists()) {
        try {
          URL updaterUrl = this.getClass().getClassLoader().getResource("Updater.jar");
          File updaterFile = new File(updaterUrl.toURI());
          if (updaterFile.length() != updater.length()) {
            updater.delete();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      if (!updater.exists()) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Updater.jar");

        if (inputStream != null) {
          try {
            Files.copy(inputStream, Paths.get(updater.toURI()), StandardCopyOption.REPLACE_EXISTING);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      threadChain.next();
    });
    threadChain.chainRunnable(() -> {
      try {
        VersionList versionList = JsonFetcher.get(Client.getClientURL() + "versions.json", VersionList.class);

        versionList.getVersions().forEach(versionObject -> {
          if (versionObject.getGameVersion().equals(Client.getGameVersion())) {
            if (versionObject.getClientBuild() > Client.getClientBuild()) {
              UpdateChecker.latestVersion = versionObject;
              UpdateChecker.upToDate = false;
            }
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
      }

      threadChain.next();
    }).chainRunnable(() -> {
      if (!ClientSettings.getValue("client.update", Boolean.class)) {
        return;
      }

      if (!UpdateChecker.isUpToDate()) {
        int answer = JOptionPane.showConfirmDialog(null, "There is an Update available, would you like to update?", "Matix Update", JOptionPane.YES_NO_OPTION);

        if (answer == 0) {
          try {
            String command = String.format(
                    "java -jar \"%s\" %s %s \"%s\"",
                    updater.getAbsolutePath(),
                    latestVersion.getGameVersion(),
                    latestVersion.getUrl(),
                    new File(".").getAbsolutePath()
            );
            Runtime.getRuntime().exec(command);
            Wrapper.getMinecraft().shutdown();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } else {
        System.out.println("Matix is up-to-date");
      }

      threadChain.next();
    }).kickOff();
  }
}

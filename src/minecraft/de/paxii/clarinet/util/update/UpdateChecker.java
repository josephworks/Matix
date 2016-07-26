package de.paxii.clarinet.util.update;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.threads.ThreadChain;
import de.paxii.clarinet.util.web.JsonFetcher;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

public class UpdateChecker {
	@Getter
	private static boolean upToDate = true;

	public UpdateChecker() {
		this.checkForUpdates();
	}

	private void checkForUpdates() {
		ThreadChain threadChain = new ThreadChain();

		threadChain.chainThread(new Thread(() -> {
			try {
				VersionList versionList = JsonFetcher.fetchData(Client.getClientURL() + "versions.json", VersionList.class);

				versionList.getVersions().forEach(versionObject -> {
					if (versionObject.getGameVersion().equals(Client.getGameVersion())) {
						if (versionObject.getClientBuild() > Client.getClientBuild()) {
							UpdateChecker.upToDate = false;
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			threadChain.next();
		})).chainThread(new Thread(() -> {
			if (!ClientSettings.getValue("client.update", Boolean.class)) {
				return;
			}

			if (!UpdateChecker.isUpToDate()) {
				int answer = JOptionPane.showConfirmDialog(null, "There is an Update available, would you like to update?", "Matix Update", JOptionPane.YES_NO_OPTION);

				if (answer == 0) {
					URL url = null;
					try {
						url = new URL(Client.getClientURL() + "download.json");
						BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

						String line;

						while ((line = br.readLine()) != null) {
							if (line.length() > 0) {
								if (line.contains("string") && line.contains("\"")) {
									String downloadURL = line.split("\"")[3];

									url = new URL(downloadURL);
								}
							}
						}
					} catch (IOException ignored) {
					}

					Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

					if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
						if (url != null) {
							try {
								desktop.browse(url.toURI());
							} catch (IOException ye) {
								JOptionPane.showMessageDialog(null, "Could not open your Browser!", "Error", JOptionPane.OK_OPTION);
								ye.printStackTrace();
							} catch (URISyntaxException ye) {
								JOptionPane.showMessageDialog(null, "Could not get download URL!", "Error", JOptionPane.OK_OPTION);
								ye.printStackTrace();
							}
						} else {
							JOptionPane.showMessageDialog(null, "Could not get download URL!", "Error", JOptionPane.OK_OPTION);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Could not open your Browser!", "Error", JOptionPane.OK_OPTION);
					}
				}
			} else {
				System.out.println("Matix is up-to-date");
			}

			threadChain.next();
		})).kickOff();
	}
}

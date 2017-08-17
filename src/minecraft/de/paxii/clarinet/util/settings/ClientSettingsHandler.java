package de.paxii.clarinet.util.settings;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.EventManager;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.util.file.FileService;

import java.io.File;
import java.io.IOException;

public class ClientSettingsHandler {
  public ClientSettingsHandler() {
    Wrapper.getEventManager().register(this);

    this.onStartGame();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> EventManager.call(new StopGameEvent())));
  }

  public void onStartGame() {
    File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/settings.json");

    try {
      if (!settingsFile.exists()) {
        settingsFile.createNewFile();
      }
      ClientSettingsContainer clientSettingsContainer = FileService.getFileContents(settingsFile, ClientSettingsContainer.class);
      if (clientSettingsContainer != null) {
        clientSettingsContainer.getClientSettings().forEach((k, v) -> {
          ClientSetting clientSetting = new ClientSetting<>(k, v);
          clientSetting = ClientSettingValueManager.patchValue(clientSetting);
          ClientSettings.getClientSettings().put(k, clientSetting);
        });
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onStopGame(StopGameEvent event) {
    try {
      File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/settings.json");
      FileService.setFileContentsAsJson(settingsFile, ClientSettingsContainer.buildContainer(ClientSettings.getClientSettings()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Wrapper.getEventManager().unregister(this);
  }
}

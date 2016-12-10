package de.paxii.clarinet.util.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.EventManager;
import de.paxii.clarinet.event.events.game.StartGameEvent;
import de.paxii.clarinet.event.events.game.StopGameEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ClientSettingsHandler {
  public ClientSettingsHandler() {
    Wrapper.getEventManager().register(this);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> EventManager.call(new StopGameEvent())));
  }

  @EventHandler
  public void onStartGame(StartGameEvent event) {
    File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/settings.json");

    try {
      Gson gson = new Gson();

      if (settingsFile.exists()) {
        BufferedReader br = new BufferedReader(new FileReader(settingsFile));
        String line, jsonString = "";

        while ((line = br.readLine()) != null) {
          jsonString += line;
        }

        ClientSettingsContainer clientSettingsContainer = gson.fromJson(jsonString, ClientSettingsContainer.class);

        if (clientSettingsContainer != null) {
          clientSettingsContainer.getClientSettings().forEach((k, v) -> {
            ClientSetting clientSetting = new ClientSetting<>(k, v);
            ClientSettings.getClientSettings().put(k, clientSetting);
          });
        }

        br.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onStopGame(StopGameEvent event) {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/settings.json");
      settingsFile.createNewFile();
      FileWriter fileWriter = new FileWriter(settingsFile);

      String jsonString = gson.toJson(ClientSettingsContainer.buildContainer(ClientSettings.getClientSettings()));
      fileWriter.write(jsonString);
      fileWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    Wrapper.getEventManager().unregister(this);
  }
}

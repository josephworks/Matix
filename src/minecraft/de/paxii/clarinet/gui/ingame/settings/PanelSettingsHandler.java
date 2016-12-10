package de.paxii.clarinet.gui.ingame.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.client.PostLoadPanelsEvent;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.GuiPanelModuleSettings;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.DefaultClientTheme;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class PanelSettingsHandler {
  public PanelSettingsHandler() {
    Wrapper.getEventManager().register(this);
  }


  @EventHandler
  public void onPanelsLoaded(PostLoadPanelsEvent event) {
    try {
      Gson gson = new Gson();
      File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/panels.json");

      if (settingsFile.exists()) {
        BufferedReader br = new BufferedReader(new FileReader(settingsFile));
        String line, jsonString = "";

        while ((line = br.readLine()) != null) {
          jsonString += line;
        }

        PanelSettingsContainer panelSettingsContainer = gson.fromJson(jsonString, PanelSettingsContainer.class);

        if (panelSettingsContainer != null) {
          panelSettingsContainer.getPanelSettings().forEach((panelName, panel) -> {
            if (Wrapper.getClickableGui().doesPanelExist(panelName)) {
              GuiPanel guiPanel = Wrapper.getClickableGui().getGuiPanel(panelName);

              guiPanel.setPanelX(panel.getPosX());
              guiPanel.setPanelY(panel.getPosY());
              guiPanel.setOpened(panel.isOpened());
              guiPanel.setPinned(panel.isPinned());
              guiPanel.setVisible(panel.isVisible());
            }
          });
        }

        br.close();
      }

      try {
        settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/color.json");

        if (settingsFile.exists()) {
          BufferedReader br = new BufferedReader(new FileReader(settingsFile));
          String line;

          while ((line = br.readLine()) != null) {
            if (line.length() > 0) {
              String colorName = gson.fromJson(line, String.class);

              ((DefaultClientTheme) Wrapper.getClickableGui().getTheme("Default")).setCurrentColor(((DefaultClientTheme) Wrapper.getClickableGui().getTheme("Default")).getColorObject(colorName));
            }
          }

          br.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onStopGame(StopGameEvent event) {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/panels.json");
      if (settingsFile.exists() && !settingsFile.delete()) {
        return;
      }

      if (settingsFile.createNewFile()) {
        FileWriter fileWriter = new FileWriter(settingsFile);

        GuiPanel[] guiPanels = Wrapper.getClickableGui().getGuiPanels().stream()
                .filter(guiPanel -> !(guiPanel instanceof GuiPanelModuleSettings))
                .toArray(GuiPanel[]::new);
        PanelSettingsContainer panelSettingsContainer = new PanelSettingsContainer(new ArrayList<>(Arrays.asList(guiPanels)));
        String jsonString = gson.toJson(panelSettingsContainer);
        fileWriter.write(jsonString);
        fileWriter.close();

        try {
          settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/color.json");
          if (settingsFile.createNewFile()) {
            fileWriter = new FileWriter(settingsFile);

            String colorName = gson.toJson(((DefaultClientTheme) Wrapper.getClickableGui().getTheme("Default")).getCurrentColor().getColorName());
            fileWriter.append(colorName);
            fileWriter.close();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    Wrapper.getEventManager().unregister(this);
  }
}

package de.paxii.clarinet.gui.ingame.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.client.PostLoadPanelsEvent;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.GuiPanelModuleSettings;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.LegacyTheme;
import de.paxii.clarinet.util.file.FileService;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PanelSettingsHandler {
  public PanelSettingsHandler() {
    Wrapper.getEventManager().register(this);
  }


  @EventHandler
  public void onPanelsLoaded(PostLoadPanelsEvent event) {
    try {
      File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/panels.json");
      if (settingsFile.exists()) {
        PanelSettingsContainer panelSettingsContainer = FileService.getFileContents(settingsFile, PanelSettingsContainer.class);

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
      }

      settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/color.json");
      if (settingsFile.exists()) {
        String colorName = FileService.getFileContents(settingsFile, String.class);
        LegacyTheme theme = (LegacyTheme) Wrapper.getClickableGui().getTheme("Legacy");
        if (theme != null) {
          theme.setCurrentColor((theme).getColorObject(colorName));
        }
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
        GuiPanel[] guiPanels = Wrapper.getClickableGui().getGuiPanels().stream()
                .filter(guiPanel -> !(guiPanel instanceof GuiPanelModuleSettings))
                .toArray(GuiPanel[]::new);
        PanelSettingsContainer panelSettingsContainer = new PanelSettingsContainer(new ArrayList<>(Arrays.asList(guiPanels)));
        FileService.setFileContentsAsJson(settingsFile, panelSettingsContainer);

        try {
          File colorFile = new File((ClientSettings.getClientFolderPath().getValue()), "/color.json");
          if (colorFile.exists() || colorFile.createNewFile()) {
            LegacyTheme legacyTheme = (LegacyTheme) Wrapper.getClickableGui().getTheme("Legacy");

            if (legacyTheme != null) {
              String colorName = gson.toJson(legacyTheme.getCurrentColor().getColorName());
              FileService.setFileContentsAsJson(colorFile, colorName);
            }
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

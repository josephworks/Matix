package de.paxii.clarinet.util.module.settings;

import com.google.gson.JsonSyntaxException;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.client.PostLoadModulesEvent;
import de.paxii.clarinet.event.events.game.LoadWorldEvent;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.file.FileService;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ModuleSettingsHandler {
  private ArrayList<Module> enabledModules;
  private static final File SETTINGS_FOLDER = new File(ClientSettings.getClientFolderPath().getValue(), "/settings");
  private static final File OLD_SETTINGS_FILE = new File(ClientSettings.getClientFolderPath().getValue(), "/modules.json");

  public ModuleSettingsHandler() {
    this.enabledModules = new ArrayList<>();
    Wrapper.getEventManager().register(this);
  }

  @EventHandler
  public void onPostLoadModules(PostLoadModulesEvent event) {
    new Thread(() -> {
      if (!SETTINGS_FOLDER.exists()) {
        SETTINGS_FOLDER.mkdirs();

        if (OLD_SETTINGS_FILE.exists()) {
          this.loadModuleSettingsLegacy();
        }

        return;
      }

      Wrapper.getModuleManager().getModuleList().values().forEach(module -> {
        File moduleSettingsFile = getModuleSettingsFile(module.getName());
        if (!moduleSettingsFile.exists()) {
          return;
        }

        try {
          ModuleSettingsObject moduleSettings = FileService.getFileContents(moduleSettingsFile, ModuleSettingsObject.class);
          if (moduleSettings != null) {
            moduleSettings.restoreToModule(module);
            if (moduleSettings.isEnabled()) {
              this.enabledModules.add(module);
            }
          }

          module.onStartup();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (JsonSyntaxException e) {
          e.printStackTrace();
          moduleSettingsFile.delete();
        } catch (Throwable t) {
          t.printStackTrace();
        }
      });
    }).start();
  }

  @EventHandler
  public void onStopGame(StopGameEvent event) {
    Wrapper.getModuleManager().getModuleList().values().forEach(module -> {
      File settingsFile = getModuleSettingsFile(module.getName());

      try {
        settingsFile.delete();

        module.onShutdown();

        if (settingsFile.createNewFile()) {
          FileService.setFileContentsAsJson(settingsFile, new ModuleSettingsObject(module));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    Wrapper.getEventManager().unregister(this);
  }

  @EventHandler
  private void onLoadWorld(LoadWorldEvent event) {
    this.enabledModules.forEach((module) -> {
      if (!module.isEnabled()) {
        module.setEnabled(true);
      }
    });
    this.enabledModules.clear();
  }

  public static File getModuleSettingsFile(String moduleName) {
    return new File(SETTINGS_FOLDER, String.format("%s.json", moduleName.replaceAll("[\\s|\\.]", "")));
  }

  @Deprecated
  public void loadModuleSettingsLegacy() {
    try {
      if (OLD_SETTINGS_FILE.exists()) {
        ModuleSettingsContainer moduleSettingsContainer = FileService.getFileContents(OLD_SETTINGS_FILE, ModuleSettingsContainer.class);
        if (moduleSettingsContainer != null) {
          Wrapper.getModuleManager().getModuleList().values().forEach((module) -> {
            for (ModuleSettingsObject moduleSettings : moduleSettingsContainer.getModuleSettings()) {
              if (moduleSettings.getModuleName().equals(module.getName())) {
                moduleSettings.restoreToModule(module);

                if (moduleSettings.isEnabled()) {
                  this.enabledModules.add(module);
                }
                break;
              }
            }

            try {
              module.onStartup();
            } catch (Throwable t) {
              t.printStackTrace();
            }
          });
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

package de.paxii.clarinet.util.module.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.client.PostLoadModulesEvent;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.event.events.player.PlayerSendChatMessageEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.notifications.NotificationPriority;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.web.JsonFetcher;

import net.minecraft.network.play.client.CPacketChatMessage;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Data;
import lombok.Getter;

/**
 * Created by Lars on 07.02.2016.
 */
public class ModuleStore {
  private static final String moduleUrl = Client.getClientURL() + "modules/files/%s/%s/%s.jar";
  private static final TreeMap<String, ModuleEntry> moduleList = new TreeMap<>();
  @Getter
  private static ArrayList<String> modulesToDelete = new ArrayList<>();

  public ModuleStore() {
    Wrapper.getEventManager().register(this);

    this.removeModules();
  }

  public static void downloadModule(String moduleName) {
    try {
      if (ModuleStore.isModuleInstalled(moduleName)) {
        ModuleStore.removeModule(moduleName);
      }

      ModuleEntry moduleEntry = ModuleStore.moduleList.get(moduleName);
      File moduleFile = new File(ClientSettings.getClientFolderPath().getValue() + "/modules/", String.format("%s.jar", moduleName + "-" + moduleEntry.getVersion()));
      URL downloadUrl = new URL(String.format(moduleUrl, Client.getGameVersion(), moduleName, moduleName + "-" + moduleEntry.getVersion()));

      if (moduleFile.exists()) {
        if (!moduleFile.delete()) {
          return;
        }
      }

      new Thread(() -> {
        try {
          FileUtils.copyURLToFile(downloadUrl, moduleFile);
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          Chat.printClientMessage(String.format("Module %s has been downloaded. Reloading Client...", moduleName));

          Wrapper.getConsole().onChatMessage(new PlayerSendChatMessageEvent(new CPacketChatMessage(ClientSettings.getValue("client.prefix", String.class) + "reload")));
        }
      }).start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void removeModule(String moduleName) {
    try {
      String removedVersion = moduleName;
      if (Wrapper.getModuleManager().doesModuleExist(moduleName)) {
        Module module = Wrapper.getModuleManager().getModule(moduleName);
        removedVersion = moduleName + "-" + module.getVersion();

        module.setEnabled(false);
        module.onShutdown();
        Wrapper.getModuleManager().removeModule(module);

        Wrapper.getClickableGui().loadPanels();
      }

      if (!ModuleStore.modulesToDelete.contains(removedVersion)) {
        ModuleStore.modulesToDelete.add(removedVersion);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    modulesToDelete.forEach(System.out::println);
  }

  public static TreeMap<String, ModuleEntry> listModules() {
    return moduleList;
  }

  public static void fetchModules() {
    String listPath = Client.getClientURL() + "modules/modules.php";

    ModuleResponse moduleResponse = JsonFetcher.fetchData(listPath, ModuleResponse.class);

    if (moduleResponse != null) {
      moduleResponse.getModuleList().forEach((moduleEntry) -> moduleList.put(moduleEntry.getModule(), moduleEntry));
    } else {
      if (moduleList.size() == 0) {
        ModuleEntry moduleEntry = new ModuleEntry();
        moduleEntry.setModule("Error");
        moduleEntry.setDescription("Could not get list of external Modules!");
        moduleList.put("Error", moduleEntry);
      }
    }
  }

  public static boolean isModuleInstalled(String moduleName) {
    return ModuleStore.moduleList.containsKey(moduleName) && Wrapper.getModuleManager().getModuleList().containsKey(moduleName);
  }

  public static boolean isModuleUptoDate(String moduleName) {
    return ModuleStore.isModuleInstalled(moduleName) && ModuleStore.listModules().get(moduleName).getBuild() <= Wrapper.getModuleManager().getModule(moduleName).getBuildVersion();
  }

  private void removeModules() {
    File modulesToDelete = new File(ClientSettings.getClientFolderPath().getValue(), "modulesToDelete.json");

    if (modulesToDelete.exists()) {
      String jsonString = "";

      try {
        Scanner sc = new Scanner(modulesToDelete);
        Gson gson = new Gson();

        while (sc.hasNextLine())
          jsonString += sc.nextLine();

        sc.close();

        ArrayList<String> modulesToRemove = gson.fromJson(jsonString, new TypeToken<ArrayList<String>>() {
        }.getType());

        if (modulesToRemove != null) {
          modulesToRemove.forEach((moduleName -> {
            File moduleFile = new File(ClientSettings.getClientFolderPath().getValue() + "/modules/", String.format("%s.jar", moduleName));
            moduleFile.delete();

            if (moduleName.contains("-")) {
              String oldModuleName = moduleName.split("-")[0];
              File oldModuleFile = new File(ClientSettings.getClientFolderPath().getValue() + "/modules/", String.format("%s.jar", oldModuleName));

              if (oldModuleFile.exists()) {
                oldModuleFile.delete();
              }
            }
          }));
        }

        modulesToDelete.delete();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  @EventHandler
  public void onPostLoadModules(PostLoadModulesEvent event) {
    AtomicBoolean updateAvailable = new AtomicBoolean(false);
    moduleList.forEach((moduleName, moduleEntry) -> {
      if (ModuleStore.isModuleInstalled(moduleName) && !ModuleStore.isModuleUptoDate(moduleName)) {
        updateAvailable.set(true);
      }
    });

    if (updateAvailable.get()) {
      Wrapper.getClient().getNotificationManager().addNotification("There are Updates available for installed Plugins!", NotificationPriority.DANGER, 10000L);
    }
  }

  @EventHandler
  public void onShutdown(StopGameEvent event) {
    File modulesToDelete = new File(ClientSettings.getClientFolderPath().getValue(), "modulesToDelete.json");

    try {
      modulesToDelete.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (ModuleStore.modulesToDelete.size() > 0) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      String jsonString = gson.toJson(ModuleStore.modulesToDelete);

      try {
        FileWriter fw = new FileWriter(modulesToDelete);

        fw.write(jsonString);

        fw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Data
  class ModuleResponse {
    private ArrayList<ModuleEntry> moduleList;

    public ModuleResponse(ArrayList<ModuleEntry> moduleList) {
      this.moduleList = moduleList;
    }
  }
}
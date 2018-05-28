package de.paxii.clarinet.util.module.friends;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.StartGameEvent;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.util.file.FileService;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.io.File;

public class FriendSettingsHandler {
  public FriendSettingsHandler() {
    Wrapper.getEventManager().register(this);
  }

  @EventHandler
  public void onStartGame(StartGameEvent event) {
    try {
      File settingsFile = new File(ClientSettings.getClientFolderPath().getValue(), "/friends.json");

      if (settingsFile.exists()) {
        FriendObjectContainer friendObjectContainer = FileService.getFileContents(settingsFile, FriendObjectContainer.class);

        if (friendObjectContainer != null && friendObjectContainer.getFriendList() != null) {
          Wrapper.getFriendManager().setFriendList(friendObjectContainer.getFriendList());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onStopGame(StopGameEvent event) {
    try {
      File settingsFile = new File(ClientSettings.getClientFolderPath().getValue(), "/friends.json");

      if (settingsFile.exists() && !settingsFile.delete()) {
        return;
      }

      if (settingsFile.createNewFile()) {
        FriendObjectContainer friendObjectContainer = new FriendObjectContainer(Wrapper.getFriendManager().getFriends());
        FileService.setFileContentsAsJson(settingsFile, friendObjectContainer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    Wrapper.getEventManager().unregister(this);
  }
}

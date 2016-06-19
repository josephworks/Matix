package de.paxii.clarinet.util.module.friends;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.StartGameEvent;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FriendSettingsHandler {
	public FriendSettingsHandler() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onStartGame(StartGameEvent event) {
		try {
			Gson gson = new Gson();
			File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/friends.json");

			if (settingsFile.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(settingsFile));
				String line, jsonString = "";

				while ((line = br.readLine()) != null) {
					jsonString += line;
				}

				FriendObjectContainer friendObjectContainer = gson.fromJson(jsonString, FriendObjectContainer.class);

				if (friendObjectContainer != null && friendObjectContainer.getFriendList() != null) {
					Wrapper.getFriendManager().setFriendList(friendObjectContainer.getFriendList());
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
			File settingsFile = new File((ClientSettings.getClientFolderPath().getValue()), "/friends.json");

			if (settingsFile.exists() && !settingsFile.delete()) {
				return;
			}

			if (settingsFile.createNewFile()) {
				FileWriter fileWriter = new FileWriter(settingsFile);

				FriendObjectContainer friendObjectContainer = new FriendObjectContainer(Wrapper.getFriendManager().getFriends());
				String jsonString = gson.toJson(friendObjectContainer);
				fileWriter.write(jsonString);
				fileWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Wrapper.getEventManager().unregister(this);
	}
}

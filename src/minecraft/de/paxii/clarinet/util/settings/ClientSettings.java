package de.paxii.clarinet.util.settings;

import de.paxii.clarinet.util.settings.type.ClientSettingBoolean;
import de.paxii.clarinet.util.settings.type.ClientSettingString;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Data
public class ClientSettings {
	@Getter
	private static HashMap<String, ClientSetting> clientSettings = new HashMap<>();
	@Getter
	private static ClientSettingString clientFolderPath = new ClientSettingString("client.folder", "./resourcepacks/1.11 R3D Craft 256x");

	static {
		put(clientFolderPath);
		put(new ClientSettingString("client.enckey", UUID.randomUUID().toString().replaceAll("-", "")));
		put(new ClientSettingString("client.prefix", "#"));
		put(new ClientSettingString("client.guitheme", "Matix2HD"));
		put(new ClientSettingBoolean("client.hidden", false));
		put(new ClientSettingBoolean("client.update", true));
	}

	public static void put(ClientSetting clientSetting) {
		ClientSettings.clientSettings.put(clientSetting.getName(), clientSetting);
	}

	public static <T extends ClientSetting> T put(ClientSetting<T> clientSetting, Class<T> type) {
		return type.cast(ClientSettings.clientSettings.put(clientSetting.getName(), clientSetting));
	}

	public static <T extends ClientSetting> T getSetting(String settingName, Class<T> type) {
		return type.cast(ClientSettings.clientSettings.get(settingName));
	}

	public static <T> T getValue(String settingName, Class<T> type) {
		return type.cast(ClientSettings.clientSettings.get(settingName).getValue());
	}
}

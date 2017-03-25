package de.paxii.clarinet.util.settings;

import de.paxii.clarinet.util.settings.type.ClientSettingBoolean;
import de.paxii.clarinet.util.settings.type.ClientSettingInteger;
import de.paxii.clarinet.util.settings.type.ClientSettingLong;
import de.paxii.clarinet.util.settings.type.ClientSettingString;

import java.util.HashMap;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;

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
    put(new ClientSettingLong("category.player", 0xFFD31EC2L));
    put(new ClientSettingLong("category.world", 0xFF1EC0A8L));
    put(new ClientSettingLong("category.combat", 0xFFBB0711L));
    put(new ClientSettingLong("category.render", 0xFFA3F330L));
    put(new ClientSettingLong("category.movement", 0xFFDEA225L));
    put(new ClientSettingLong("category.other", 0xFF239991L));
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

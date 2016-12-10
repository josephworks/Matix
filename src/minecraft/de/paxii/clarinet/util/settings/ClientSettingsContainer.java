package de.paxii.clarinet.util.settings;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lars on 14.02.2016.
 */
public class ClientSettingsContainer {
  @Getter
  @Setter
  private HashMap<String, Object> clientSettings;

  public ClientSettingsContainer(HashMap<String, Object> clientSettings) {
    this.clientSettings = clientSettings;
  }

  public static ClientSettingsContainer buildContainer(HashMap<String, ClientSetting> clientSettingHashMap) {
    ClientSettingsContainer clientSettingsContainer = new ClientSettingsContainer(new HashMap<>());

    clientSettingHashMap.forEach((k, v) -> clientSettingsContainer.getClientSettings().put(k, v.getValue()));

    return clientSettingsContainer;
  }
}

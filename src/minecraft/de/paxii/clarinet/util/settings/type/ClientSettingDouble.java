package de.paxii.clarinet.util.settings.type;

import de.paxii.clarinet.util.settings.ClientSetting;

/**
 * Created by Lars on 18.06.2016.
 */
public class ClientSettingDouble extends ClientSetting<Double> {
  public ClientSettingDouble(String settingName, Double settingValue) {
    super(settingName, settingValue);
  }
}

package de.paxii.clarinet.util.settings.type;

import de.paxii.clarinet.util.settings.ClientSetting;

/**
 * Created by Lars on 15.06.2016.
 */
public class ClientSettingLong extends ClientSetting<Long> {
  public ClientSettingLong(String settingName, Long settingValue) {
    super(settingName, settingValue);
  }
}

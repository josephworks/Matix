package de.paxii.clarinet.util.settings.type;

import de.paxii.clarinet.util.settings.ClientSetting;

/**
 * Created by Lars on 15.06.2016.
 */
public class ClientSettingString extends ClientSetting<String> {
	public ClientSettingString(String settingName, String settingValue) {
		super(settingName, settingValue);
	}
}

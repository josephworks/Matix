package de.paxii.clarinet.util.settings.type;

import de.paxii.clarinet.util.settings.ClientSetting;

/**
 * Created by Lars on 15.06.2016.
 */
public class ClientSettingBoolean extends ClientSetting<Boolean> {
	public ClientSettingBoolean(String settingName, Boolean settingValue) {
		super(settingName, settingValue);
	}
}

package de.paxii.clarinet.util.settings.type;

import de.paxii.clarinet.util.settings.ClientSetting;

/**
 * Created by Lars on 15.06.2016.
 */
public class ClientSettingInteger extends ClientSetting<Integer> {
	public ClientSettingInteger(String settingName, Integer settingValue) {
		super(settingName, settingValue);
	}
}

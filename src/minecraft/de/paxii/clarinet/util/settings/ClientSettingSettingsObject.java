package de.paxii.clarinet.util.settings;

import lombok.Data;

/**
 * Created by Lars on 03.08.2016.
 */
@Data
public class ClientSettingSettingsObject {
	private String name;
	private Object value;

	public ClientSettingSettingsObject(String name, Object value) {
		this.name = name;
		this.value = value;
	}
}

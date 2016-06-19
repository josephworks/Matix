package de.paxii.clarinet.util.settings;

import lombok.Data;

@Data
public class ClientSetting<T> {
	private String name;
	private T value;

	public ClientSetting(String name, T value) {
		this.name = name;
		this.value = value;
	}

	@Deprecated
	public String getSettingName() {
		return name;
	}

	@Deprecated
	public void setSettingName(String settingName) {
		this.name = settingName;
	}

	@Deprecated
	public T getSettingValue() {
		return value;
	}

	@Deprecated
	public void setSettingValue(T settingValue) {
		this.value = settingValue;
	}
}

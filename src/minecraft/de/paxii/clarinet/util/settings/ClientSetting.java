package de.paxii.clarinet.util.settings;

import lombok.Data;

@Data
public class ClientSetting<T> implements Comparable<ClientSetting> {
	private String name;
	private T value;

	public ClientSetting(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public void setValue(T value) {
		if (value != this.value || !this.value.equals(value)) {
			this.onUpdate(value, this.value);
		}
		this.value = value;
	}

	public void onUpdate(T newValue, T oldValue) {}

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

	@Override
	public int compareTo(ClientSetting otherSetting) {
		return this.getName().compareTo(otherSetting.getName());
	}
}

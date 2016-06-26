package de.paxii.clarinet.module;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.settings.ClientSetting;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class Module implements Comparable<Module> {
	@Getter
	private String name;
	@Getter
	@Setter
	private ModuleCategory category;
	@Getter
	private int key;
	@Getter
	@Setter
	private String syntax, description;
	@Getter
	@Setter
	private String helpUrl;
	@Getter
	@Setter
	private boolean command;
	@Getter
	private boolean enabled;
	@Getter
	@Setter
	private boolean displayedInGui;
	@Getter
	@Setter
	private boolean plugin;
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	private int buildVersion;
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	private String version;
	@Getter
	@Setter
	private HashMap<String, ValueBase> moduleValues;
	@Getter
	@Setter
	private HashMap<String, ClientSetting> moduleSettings;

	public Module(String name, ModuleCategory category) {
		this.name = name;
		this.category = category;
		this.setKey(-1);
		this.setHelpUrl(Client.getClientURL() + "docs/modules/" + this.category.toString().toLowerCase() + "/" + this.getName().toLowerCase() + ".html");
		this.setBuildVersion(Client.getClientBuild());
		this.setVersion(Client.getClientVersion());

		this.moduleValues = new HashMap<>();
		this.moduleSettings = new HashMap<>();
		this.setDisplayedInGui(true);
	}

	public Module(String name, ModuleCategory category, int i) {
		this(name, category);

		this.setKey(i);
	}

	public final void setEnabled(boolean enabled) {
		if (this.enabled == enabled)
			return;

		this.enabled = enabled;

		if (isEnabled()) {
			this.onEnable();
		} else {
			this.onDisable();
		}

		this.onToggle();
	}

	public final void setKey(int key) {
		this.key = key;

		if (key != -1) {
			Wrapper.getModuleManager().addKey(this.name, key);
		} else {
			Wrapper.getModuleManager().removeKey(this.name);
		}
	}

	public final void toggle() {
		setEnabled(!isEnabled());
	}

	public void onEnable() {
	}

	public void onDisable() {
	}

	public void onToggle() {
	}

	public void onStartup() {
	}

	public void onShutdown() {
	}

	public void onCommand(String[] args) {
	}

	public String toString() {
		return this.getName();
	}

	public <T extends ClientSetting> T getSetting(String settingName, Class<T> type) {
		return type.cast(this.moduleSettings.get(settingName));
	}

	public <T> T getValue(String settingName, Class<T> type) {
		return type.cast(this.moduleSettings.get(settingName).getValue());
	}

	public <T> T getValueOrDefault(String settingName, Class<T> type, T defaultValue) {
		if (this.moduleSettings.containsKey(settingName)) {
			return type.cast(this.moduleSettings.get(settingName).getValue());
		}

		return defaultValue;
	}

	@Override
	public int compareTo(Module o) {
		return this.getName().compareToIgnoreCase(o.getName());
	}
}

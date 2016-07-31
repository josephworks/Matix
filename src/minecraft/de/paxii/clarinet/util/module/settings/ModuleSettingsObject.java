package de.paxii.clarinet.util.module.settings;

import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.settings.ClientSetting;
import lombok.Getter;

import java.util.HashMap;

public class ModuleSettingsObject {
	@Getter
	private String moduleName;
	@Getter
	private int moduleKey;
	@Getter
	private boolean enabled;
	@Getter
	private HashMap<String, ClientSetting> moduleSettings;
	@Getter
	private HashMap<String, ValueBaseSettingsObject> moduleValues;

	public ModuleSettingsObject(Module module) {
		this(module.getName(), module.getKey(), module.isEnabled(), module.getModuleSettings(), module.getModuleValues());
	}

	public ModuleSettingsObject(String moduleName, int moduleKey, boolean enabled, HashMap<String, ClientSetting> moduleSettings, HashMap<String, ValueBase> moduleValues) {
		this.moduleName = moduleName;
		this.moduleKey = moduleKey;
		this.enabled = enabled;
		this.moduleSettings = moduleSettings;
		this.moduleValues = new HashMap<>();

		moduleValues.forEach((valueName, valueBase) -> this.moduleValues.put(valueName, new ValueBaseSettingsObject(valueBase)));
	}
}

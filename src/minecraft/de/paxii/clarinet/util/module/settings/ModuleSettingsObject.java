package de.paxii.clarinet.util.module.settings;

import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.settings.ClientSetting;
import de.paxii.clarinet.util.settings.ClientSettingSettingsObject;

import java.util.HashMap;

import lombok.Getter;

public class ModuleSettingsObject {
  @Getter
  private String moduleName;
  @Getter
  private int moduleKey;
  @Getter
  private boolean enabled;
  @Getter
  private HashMap<String, ClientSettingSettingsObject> moduleSettings;
  @Getter
  private HashMap<String, ValueBaseSettingsObject> moduleValues;

  public ModuleSettingsObject(Module module) {
    this(module.getName(), module.getKey(), module.isEnabled(), module.getModuleSettings(), module.getModuleValues());
  }

  public ModuleSettingsObject(String moduleName, int moduleKey, boolean enabled, HashMap<String, ClientSetting> moduleSettings, HashMap<String, ValueBase> moduleValues) {
    this.moduleName = moduleName;
    this.moduleKey = moduleKey;
    this.enabled = enabled;
    this.moduleSettings = new HashMap<>();
    this.moduleValues = new HashMap<>();

    moduleSettings.forEach((settingsName, setting) -> this.moduleSettings.put(settingsName, new ClientSettingSettingsObject(setting.getName(), setting.getValue())));
    moduleValues.forEach((valueName, valueBase) -> this.moduleValues.put(valueName, new ValueBaseSettingsObject(valueBase)));
  }
}

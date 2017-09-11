package de.paxii.clarinet.util.module.settings;

import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.settings.ClientSetting;
import de.paxii.clarinet.util.settings.ClientSettingSettingsObject;
import de.paxii.clarinet.util.settings.ClientSettingValueManager;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class ModuleSettingsObject {
  @Getter
  @Deprecated
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

  public void restoreToModule(Module module) {
    for (Map.Entry<String, ClientSettingSettingsObject> settingsEntry : this.getModuleSettings().entrySet()) {
      settingsEntry.getValue().setValue(ClientSettingValueManager.patchValue(settingsEntry.getValue().getValue()));
      module.setValue(settingsEntry.getKey(), settingsEntry.getValue().getValue());
    }

    this.getModuleValues().forEach((k, v) -> {
      if (ValueBase.doesValueExist(v.getName())) {
        ValueBase vb = ValueBase.getValueBase(v.getName());

        if (vb != null) {
          vb.setValue(v.getValue());
          vb.setMin(v.getMin());
          vb.setMax(v.getMax());
          vb.setName(v.getName());
        }
      }
    });

    module.setKey(this.getModuleKey());
  }
}

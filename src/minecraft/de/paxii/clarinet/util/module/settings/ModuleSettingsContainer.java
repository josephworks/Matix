package de.paxii.clarinet.util.module.settings;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Created by Lars on 02.02.2016.
 */
public class ModuleSettingsContainer {
	@Getter
	@Setter
	private ArrayList<ModuleSettingsObject> moduleSettings;

	public ModuleSettingsContainer(ArrayList<ModuleSettingsObject> moduleSettings) {
		this.moduleSettings = moduleSettings;
	}
}

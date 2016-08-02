package de.paxii.clarinet.module.other;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lars on 20.03.2016.
 */
public class ModulePanic extends Module {
	private ArrayList<Module> enabledModules;

	public ModulePanic() {
		super("Panic", ModuleCategory.OTHER);

		this.enabledModules = new ArrayList<>();

		this.setDescription("Turns all modules off.");
	}

	@Override
	public void onEnable() {
		this.enabledModules.clear();

		Wrapper.getModuleManager().getModuleList().values().forEach(module -> {
			if (module.isEnabled() && module != this) {
				this.enabledModules.add(module);
				module.setEnabled(false);
			}
		});
	}

	@Override
	public void onDisable() {
		for (Module module : this.enabledModules) {
			module.setEnabled(true);
		}
	}
}

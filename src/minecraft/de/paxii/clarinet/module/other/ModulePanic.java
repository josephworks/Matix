package de.paxii.clarinet.module.other;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

/**
 * Created by Lars on 20.03.2016.
 */
public class ModulePanic extends Module {
	public ModulePanic() {
		super("Panic", ModuleCategory.OTHER);

		this.setDescription("Turns all modules off.");
	}

	@Override
	public void onEnable() {
		Wrapper.getModuleManager().getModuleList().forEach((moduleName, module) -> {
			if (module.isEnabled()) {
				module.setEnabled(false);
			}
		});
	}
}

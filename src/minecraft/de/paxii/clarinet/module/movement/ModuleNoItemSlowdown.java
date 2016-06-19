package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

/**
 * Created by Lars on 06.03.2016.
 */
public class ModuleNoItemSlowdown extends Module {
	public ModuleNoItemSlowdown() {
		super("NoItemSlowdown", ModuleCategory.MOVEMENT);

		this.setDescription("Allows you to run while using items or eating food.");
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

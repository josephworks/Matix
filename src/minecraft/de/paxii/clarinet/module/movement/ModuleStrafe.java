package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import lombok.Getter;

/**
 * Created by Lars on 13.08.2016.
 */
public class ModuleStrafe extends Module {
	@Getter
	private static boolean isActive;

	public ModuleStrafe() {
		super("Strafe", ModuleCategory.MOVEMENT);

		this.setRegistered(true);
		this.setDescription("Allows you to move mid-air as you were on the ground");
	}

	@Override
	public void onEnable() {
		ModuleStrafe.isActive = true;
	}

	@EventHandler
	public void onIngameTick(IngameTickEvent tickEvent) {
		if (Wrapper.getModuleManager().isModuleActive("Fly")) {
			ModuleStrafe.isActive = false;
		} else {
			ModuleStrafe.isActive = true;
		}
	}

	@Override
	public void onDisable() {
		ModuleStrafe.isActive = false;
	}
}

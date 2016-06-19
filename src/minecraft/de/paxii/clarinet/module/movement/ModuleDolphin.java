package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class ModuleDolphin extends Module {
	public ModuleDolphin() {
		super("Dolphin", ModuleCategory.MOVEMENT, Keyboard.KEY_J);

		this.setDescription("Automatically swims for you.");
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onTick(IngameTickEvent event) {
		if (!Wrapper.getGameSettings().keyBindSneak.isKeyDown()
				&& Wrapper.getPlayer().isInWater()) {
			if (!Wrapper.getPlayer().isCollidedHorizontally) {
				Wrapper.getPlayer().motionY = 0.04D;
			} else {
				//Wrapper.getPlayer().jump();
				Wrapper.getPlayer().motionY += 0.0D;
			}
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

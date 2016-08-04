package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.module.settings.ValueBase;
import org.lwjgl.input.Keyboard;

public class ModuleFly extends Module {
	public ModuleFly() {
		super("Fly", ModuleCategory.MOVEMENT, Keyboard.KEY_R);

		this.setDescription("Allows you to fly.");

		this.getModuleValues().put("flySpeed", new ValueBase("Fly Speed", 0.5F, 0.1F, 5.0F, "Speed"));
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onTick(IngameTickEvent event) {
		if (Wrapper.getPlayer().isInWater())
			return;

		float flightSpeed = this.getModuleValues().get("flySpeed").getValue();

		Wrapper.getPlayer().jumpMovementFactor = flightSpeed;

		Wrapper.getPlayer().motionY = 0.0D;
		Wrapper.getPlayer().motionX = 0.0D;
		Wrapper.getPlayer().motionZ = 0.0D;

		if (Wrapper.getGameSettings().keyBindJump.isKeyDown()) {
			Wrapper.getPlayer().motionY += flightSpeed / 1.2;
		} else if (Wrapper.getGameSettings().keyBindSneak.isKeyDown()) {
			Wrapper.getPlayer().motionY -= flightSpeed / 1.2;
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

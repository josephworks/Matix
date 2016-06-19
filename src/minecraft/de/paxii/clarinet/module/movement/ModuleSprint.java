package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PlayerMoveEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class ModuleSprint extends Module {
	public ModuleSprint() {
		super("Sprint", ModuleCategory.MOVEMENT, Keyboard.KEY_N);

		this.setDescription("Automatically sprints for you.");
	}

	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		boolean canSprint =
				(event.getPlayer().moveForward > 0) &&
						(event.getPlayer().getFoodStats().getFoodLevel() > 6 || event.getPlayer().capabilities.isCreativeMode) &&
						!event.getPlayer().capabilities.isFlying && !event.getPlayer().isCollidedHorizontally;
		event.getPlayer().setSprinting(canSprint);
	}

	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

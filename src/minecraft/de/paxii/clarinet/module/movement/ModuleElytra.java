package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PlayerMoveEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * Created by Lars on 06.03.2016.
 */
public class ModuleElytra extends Module {
	public ModuleElytra() {
		super("Elytra", ModuleCategory.MOVEMENT);

		this.setDescription("Allows you to do an upwards motion with an elytra when holding the jump button.");
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onMovement(PlayerMoveEvent event) {
		if (!event.getPlayer().onGround
				&& event.getPlayer().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA
				&& Wrapper.getGameSettings().keyBindJump.isKeyDown()) {
			event.setMotionY(event.getMotionY() + 0.05);
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

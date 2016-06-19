package de.paxii.clarinet.module.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.entity.EntityVelocityEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class ModuleAutoFish extends Module {
	public ModuleAutoFish() {
		super("AutoFish", ModuleCategory.PLAYER, -1);

		this.setDescription("Autmatically fishes for you.");
	}

	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onEntityVelocity(EntityVelocityEvent event) {
		Entity e = Wrapper.getWorld().getEntityByID(event.getEntityID());

		if (e instanceof EntityFishHook) {
			EntityFishHook fishHook = (EntityFishHook) e;

			if (fishHook.angler.getEntityId() == Wrapper.getPlayer()
					.getEntityId()) {
				if (event.getVelocityPacket().getMotionX() == 0
						&& event.getVelocityPacket().getMotionY() != 0
						&& event.getVelocityPacket().getMotionZ() == 0) {
					(new Thread(new ThreadAutoFish())).start();
				}
			}
		}
	}

	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

class ThreadAutoFish implements Runnable {
	@Override
	public void run() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Wrapper.getSendQueue().addToSendQueue(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Wrapper.getSendQueue().addToSendQueue(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
	}
}

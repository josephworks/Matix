package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.event.events.game.SendPacketEvent;
import de.paxii.clarinet.event.events.player.PlayerStepEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleStep extends Module {
	public ModuleStep() {
		super("Step", ModuleCategory.MOVEMENT, Keyboard.KEY_K);

		this.setRegistered(true);
		this.setDescription("Allows you to step up full blocks.");
	}

	private final AtomicBoolean canPacket = new AtomicBoolean();

	@EventHandler
	public void onTick(IngameTickEvent event) {
		Wrapper.getPlayer().stepHeight = Wrapper.getPlayer().onGround ? 1.0F : 0.5F;
	}

	@EventHandler
	public void onStep(PlayerStepEvent event) {
		this.canPacket.set(Wrapper.getPlayer().onGround && event.getStepHeight() >= 1);
	}

	@EventHandler
	public void onPacketSend(SendPacketEvent event) {
		if (event.getPacket() instanceof CPacketPlayer && this.canPacket.compareAndSet(true, false)) {
			CPacketPlayer curPacket = (CPacketPlayer) event.getPacket();
			curPacket.setY(curPacket.getY() + 0.063);
			event.setPacket(curPacket);
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getPlayer().stepHeight = 0.5F;
	}
}

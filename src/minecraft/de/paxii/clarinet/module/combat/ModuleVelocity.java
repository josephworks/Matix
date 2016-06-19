package de.paxii.clarinet.module.combat;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.entity.EntityVelocityEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.module.settings.ValueBase;

public class ModuleVelocity extends Module {
	public ModuleVelocity() {
		super("Velocity", ModuleCategory.COMBAT, -1);

		this.setDescription("Changes the amount of knockback you take.");

		this.getModuleValues().put("velocityValue", new ValueBase("Velocity (%)", 50.0F, 0.0F, 100.0F, true));
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onVelocity(EntityVelocityEvent event) {
		int playerID = Wrapper.getPlayer().getEntityId();

		if (event.getEntityID() == playerID) {
			float reduction = this.calculateReduction();
			event.getVelocityPacket().setMotionX((int) (event.getVelocityPacket().getMotionX() * reduction));
			event.getVelocityPacket().setMotionY((int) (event.getVelocityPacket().getMotionY() * reduction));
			event.getVelocityPacket().setMotionZ((int) (event.getVelocityPacket().getMotionZ() * reduction));
		}
	}

	private float calculateReduction() {
		return (this.getModuleValues().get("velocityValue").getValue() / 100.0F);
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

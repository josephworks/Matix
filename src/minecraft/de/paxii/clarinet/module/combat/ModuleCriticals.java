package de.paxii.clarinet.module.combat;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PlayerAttackEntityEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

/**
 * Created by Lars on 04.02.2016.
 */
public class ModuleCriticals extends Module {
	private final float jumpDistance = 0.07F;

	public ModuleCriticals() {
		super("Criticals", ModuleCategory.COMBAT);

		this.setDescription("Forces criticals on entity attack.");
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onAttack(PlayerAttackEntityEvent event) {
		if (event.getSource().onGround &&
				!(event.getSource().handleWaterMovement() && (event.getSource().isInLava()))) {
			event.getSource().onGround = false;
			event.getSource().motionY = jumpDistance;
			event.getSource().fallDistance = jumpDistance;
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.type.EventCancellable;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;

public class PreMotionUpdateEvent extends EventCancellable {
	@Getter
	private EntityPlayer player;

	public PreMotionUpdateEvent(EntityPlayer entityPlayer) {
		this.player = entityPlayer;
	}
}

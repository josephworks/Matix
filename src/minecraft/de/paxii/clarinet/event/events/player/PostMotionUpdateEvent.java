package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.type.EventCancellable;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;

public class PostMotionUpdateEvent extends EventCancellable {
	@Getter
	private EntityPlayer player;

	public PostMotionUpdateEvent(EntityPlayer entityPlayer) {
		this.player = entityPlayer;
	}
}

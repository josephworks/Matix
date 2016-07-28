package de.paxii.clarinet.event.events.entity;

import de.paxii.clarinet.event.events.Event;
import lombok.Data;
import net.minecraft.entity.Entity;

@Data
public class EntityMoveEvent implements Event {
	private Entity entity;
	private double x, y, z;
	private boolean noClip;

	public EntityMoveEvent(Entity entity, double x, double y, double z, boolean noClip) {
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.noClip = noClip;
	}
}

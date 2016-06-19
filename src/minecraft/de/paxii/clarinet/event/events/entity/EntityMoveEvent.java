package de.paxii.clarinet.event.events.entity;

import de.paxii.clarinet.event.events.Event;
import lombok.Getter;
import lombok.Setter;

public class EntityMoveEvent implements Event {
	@Getter
	@Setter
	private double x, y, z;

	public EntityMoveEvent(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}

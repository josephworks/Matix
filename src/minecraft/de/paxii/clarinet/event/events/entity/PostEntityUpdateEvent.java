package de.paxii.clarinet.event.events.entity;

import de.paxii.clarinet.event.events.Event;
import lombok.Data;
import net.minecraft.entity.Entity;

/**
 * Created by Lars on 08.08.2016.
 */
@Data
public class PostEntityUpdateEvent implements Event {
	private Entity entity;

	public PostEntityUpdateEvent(Entity entity) {
		this.entity = entity;
	}
}

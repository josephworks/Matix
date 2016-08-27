package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.Event;
import lombok.Data;

/**
 * Created by Lars on 27.08.2016.
 */
@Data
public class UpdatePlayerMoveStateEvent implements Event {
	private float moveStrafe, moveForward;

	public UpdatePlayerMoveStateEvent(float moveStrafe, float moveForward) {
		this.moveStrafe = moveStrafe;
		this.moveForward = moveForward;
	}
}

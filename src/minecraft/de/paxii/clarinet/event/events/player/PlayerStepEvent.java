package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.Event;
import lombok.Getter;

public class PlayerStepEvent implements Event {
	@Getter
	private double stepHeight;

	public PlayerStepEvent(double stepHeight) {
		this.stepHeight = stepHeight;
	}
}

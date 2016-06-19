package de.paxii.clarinet.event.events.game;

import de.paxii.clarinet.event.events.Event;
import lombok.Getter;

public class RenderTickEvent implements Event {
	@Getter
	private float renderPartialTicks;

	public RenderTickEvent(float renderPartialTicks) {
		this.renderPartialTicks = renderPartialTicks;
	}
}

package de.paxii.clarinet.event.events.gui;

import de.paxii.clarinet.event.events.Event;
import lombok.Getter;
import lombok.Setter;


public class RenderGuiScreenEvent implements Event {
	@Getter
	@Setter
	int mouseX, mouseY;
	@Getter
	private float renderPartialTicks;

	public RenderGuiScreenEvent(int mouseX, int mouseY, float renderPartialTicks) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.renderPartialTicks = renderPartialTicks;
	}
}

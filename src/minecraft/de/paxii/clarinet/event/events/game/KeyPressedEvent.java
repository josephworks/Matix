package de.paxii.clarinet.event.events.game;

import de.paxii.clarinet.event.events.Event;

public class KeyPressedEvent implements Event {
	private int keyPressed;

	public KeyPressedEvent(int keyPressed) {
		this.keyPressed = keyPressed;
	}

	public int getKey() {
		return this.keyPressed;
	}
}

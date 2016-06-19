package de.paxii.clarinet.event.events.type;

import de.paxii.clarinet.event.events.Event;


public class EventCancellable implements Event {
	private boolean cancelled;

	public void setCancelled(boolean par1) {
		this.cancelled = par1;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}
}

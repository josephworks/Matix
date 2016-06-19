package de.paxii.clarinet.event.events.game;

import de.paxii.clarinet.event.events.type.EventCancellable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

public class SendPacketEvent extends EventCancellable {

	@Getter
	@Setter
	private Packet packet;

	public SendPacketEvent(Packet packet) {
		this.packet = packet;
	}
}

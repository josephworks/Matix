package de.paxii.clarinet.event.events.chat;

import de.paxii.clarinet.event.events.type.EventCancellable;
import lombok.Getter;
import lombok.Setter;

public class ReceiveChatEvent extends EventCancellable {
	//@Getter @Setter private String chatSender;
	@Getter
	@Setter
	private String chatMessage;

	public ReceiveChatEvent(/*String chatSender, */String chatMessage) {
		//this.chatSender = chatSender;
		this.chatMessage = chatMessage;
	}
}

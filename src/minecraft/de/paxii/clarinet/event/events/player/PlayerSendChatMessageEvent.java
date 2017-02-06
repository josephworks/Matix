package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.type.EventCancellable;

import net.minecraft.network.play.client.C01PacketChatMessage;

public class PlayerSendChatMessageEvent extends EventCancellable {
  private C01PacketChatMessage chatPacket;

  public PlayerSendChatMessageEvent(C01PacketChatMessage chatPacket) {
    this.chatPacket = chatPacket;
  }

  public String getChatMessage() {
    return this.chatPacket.getMessage();
  }

  public void setChatMessage(String newMessage) {
    this.chatPacket = new C01PacketChatMessage(newMessage);
  }

  public C01PacketChatMessage getPacket() {
    return this.chatPacket;
  }
}

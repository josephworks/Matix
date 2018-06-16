package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.type.EventCancellable;

import net.minecraft.network.play.client.CPacketChatMessage;

public class PlayerSendChatMessageEvent extends EventCancellable {
  public static final String BYPASS_PREFIX = "_PASS_";

  private CPacketChatMessage chatPacket;

  public PlayerSendChatMessageEvent(CPacketChatMessage chatPacket) {
    this.chatPacket = chatPacket;
  }

  public String getChatMessage() {
    return this.chatPacket.getMessage();
  }

  public void setChatMessage(String newMessage) {
    this.chatPacket = new CPacketChatMessage(newMessage);
  }

  public CPacketChatMessage getPacket() {
    return this.chatPacket;
  }
}

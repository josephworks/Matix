package de.paxii.clarinet.event;

import de.paxii.clarinet.event.events.game.SendPacketEvent;
import de.paxii.clarinet.event.events.player.PlayerSendChatMessageEvent;
import de.paxii.clarinet.event.events.player.PlayerSwingItemEvent;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class PacketPatcher {
  @Getter
  private static ArrayList<Class<? extends Packet>> filterList = new ArrayList<>();
  @Getter
  @Setter
  private static boolean shouldFilter = false;

  public static Packet getPatchedPacket(Packet packetIn) {
    Packet packetOut = packetIn;

    SendPacketEvent packageEvent = new SendPacketEvent(packetIn);
    EventManager.call(packageEvent);

    if (packageEvent.isCancelled())
      return null;

    if (isShouldFilter()) {
      for (Class blockedPacket : filterList) {
        if (packetIn.getClass() == blockedPacket) {
          return null;
        }
      }
    }

    if (packetIn instanceof CPacketAnimation) {
      EventManager.call(new PlayerSwingItemEvent());
    }

    if (packetIn instanceof CPacketChatMessage) {
      CPacketChatMessage tempPacket = (CPacketChatMessage) packetIn;
      String bypassPrefix = "_PASS_";

      if (tempPacket.getMessage().startsWith(bypassPrefix)) {
        String chatMessage = tempPacket.getMessage().substring(bypassPrefix.length());
        packetOut = new CPacketChatMessage(chatMessage);
      } else {
        PlayerSendChatMessageEvent messageEvent = new PlayerSendChatMessageEvent(tempPacket);
        EventManager.call(messageEvent);

        if (messageEvent.isCancelled()) {
          return null;
        }

        packetOut = messageEvent.getPacket();
      }
    }

    return packetOut;
  }
}

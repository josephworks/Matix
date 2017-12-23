package de.paxii.clarinet.event.events.game;

import de.paxii.clarinet.event.events.type.EventCancellable;

import net.minecraft.network.Packet;

import lombok.Getter;
import lombok.Setter;

public class ReceivePacketEvent extends EventCancellable {

  @Getter
  @Setter
  private Packet packet;

  public ReceivePacketEvent(Packet packet) {
    this.packet = packet;
  }
}

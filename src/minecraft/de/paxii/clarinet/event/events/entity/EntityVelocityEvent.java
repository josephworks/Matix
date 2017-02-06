package de.paxii.clarinet.event.events.entity;

import de.paxii.clarinet.event.events.type.EventCancellable;

import net.minecraft.network.play.server.S12PacketEntityVelocity;

import lombok.Getter;
import lombok.Setter;

public class EntityVelocityEvent extends EventCancellable {
  @Getter
  @Setter
  private int entityID;
  @Getter
  @Setter
  private S12PacketEntityVelocity velocityPacket;

  public EntityVelocityEvent(S12PacketEntityVelocity velocityPacket) {
    this.entityID = velocityPacket.getEntityID();
    this.velocityPacket = velocityPacket;
  }
}

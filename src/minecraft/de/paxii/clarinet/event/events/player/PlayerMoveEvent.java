package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.type.EventCancellable;

import net.minecraft.client.entity.EntityPlayerSP;

import lombok.Data;

@Data
public class PlayerMoveEvent extends EventCancellable {
  private EntityPlayerSP player;
  private double motionX, motionY, motionZ;
  private double posX, posY, posZ;

  public PlayerMoveEvent(EntityPlayerSP player) {
    this.player = player;
    this.motionX = this.player.motionX;
    this.motionY = this.player.motionY;
    this.motionZ = this.player.motionZ;

    this.posX = this.player.posX;
    this.posY = this.player.posY;
    this.posZ = this.player.posZ;
  }
}

package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.type.EventCancellable;

import net.minecraft.entity.player.EntityPlayer;

import lombok.Getter;

public class PreMotionUpdateEvent extends EventCancellable {
  @Getter
  private EntityPlayer player;

  public PreMotionUpdateEvent(EntityPlayer entityPlayer) {
    this.player = entityPlayer;
  }
}

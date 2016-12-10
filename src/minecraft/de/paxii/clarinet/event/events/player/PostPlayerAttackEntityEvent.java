package de.paxii.clarinet.event.events.player;

import de.paxii.clarinet.event.events.Event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import lombok.Getter;


public class PostPlayerAttackEntityEvent implements Event {
  @Getter
  private EntityPlayer sourceEntity;
  @Getter
  private Entity targetEntity;

  public PostPlayerAttackEntityEvent(EntityPlayer sourceEntity, Entity targetEntity) {
    this.sourceEntity = sourceEntity;
    this.targetEntity = targetEntity;
  }
}

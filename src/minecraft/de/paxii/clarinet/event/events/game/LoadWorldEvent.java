package de.paxii.clarinet.event.events.game;

import de.paxii.clarinet.event.events.Event;

import net.minecraft.world.World;

import lombok.Getter;

/**
 * Created by Lars on 31.07.2016.
 */
public class LoadWorldEvent implements Event {
  @Getter
  private final World world;

  public LoadWorldEvent(World world) {
    this.world = world;
  }
}

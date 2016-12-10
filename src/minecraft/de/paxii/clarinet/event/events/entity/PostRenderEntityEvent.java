package de.paxii.clarinet.event.events.entity;

import de.paxii.clarinet.event.events.type.EventCancellable;

import net.minecraft.entity.Entity;

import lombok.Getter;
import lombok.Setter;

public class PostRenderEntityEvent extends EventCancellable {
  @Getter
  private Entity renderedEntity;
  @Getter
  private float renderPartialTicks;
  @Getter
  private double x, y, z;
  @Getter
  @Setter
  private boolean debugRendered;

  public PostRenderEntityEvent(Entity renderedEntity, double x, double y, double z, float renderPartialTicks) {
    this.renderedEntity = renderedEntity;
    this.x = x;
    this.y = y;
    this.z = z;
    this.renderPartialTicks = renderPartialTicks;
  }
}

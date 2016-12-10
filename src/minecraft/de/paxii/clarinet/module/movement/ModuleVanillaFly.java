package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PreMotionUpdateEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

public class ModuleVanillaFly extends Module {
  private double savedX, savedY, savedZ;

  public ModuleVanillaFly() {
    super("VanillaFly", ModuleCategory.MOVEMENT, -1);

    this.setRegistered(true);
    this.setDescription("Delays the \"Kicked for flying\" - Vanilla AntiFly.");
  }

  @EventHandler
  public void preMotionUpdate(PreMotionUpdateEvent event) {
    if (event.getPlayer().onGround || Wrapper.getModuleManager().isModuleActive("KillAura")) {
      return;
    }

    double distanceX = event.getPlayer().posX - this.savedX;
    double distanceY = event.getPlayer().posY - this.savedY;
    double distanceZ = event.getPlayer().posZ - this.savedZ;

    double distanceSq = (distanceX * distanceX) + (distanceY * distanceY) + (distanceZ * distanceZ);

    if (distanceSq <= 80.0D) {
      event.setCancelled(true);
      return;
    }

    this.savedX = event.getPlayer().posX;
    this.savedY = event.getPlayer().posY;
    this.savedZ = event.getPlayer().posZ;
  }
}

package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PlayerMoveEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

import org.lwjgl.input.Keyboard;

public class ModuleSprint extends Module {
  public ModuleSprint() {
    super("Sprint", ModuleCategory.MOVEMENT, Keyboard.KEY_N);

    this.setRegistered(true);
    this.setDescription("Automatically sprints for you.");
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    boolean canSprint =
            (event.getPlayer().movementInput.moveForward > 0) &&
                    (event.getPlayer().getFoodStats().getFoodLevel() > 6 || event.getPlayer().capabilities.isCreativeMode) &&
                    !event.getPlayer().capabilities.isFlying && !event.getPlayer().isCollidedHorizontally;
    event.getPlayer().setSprinting(canSprint);
  }
}

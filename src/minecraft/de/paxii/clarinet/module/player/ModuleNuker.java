package de.paxii.clarinet.module.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.module.killaura.TimeManager;

import net.minecraft.block.Block;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;

public class ModuleNuker extends Module {
  private final TimeManager timeManager;

  public ModuleNuker() {
    super("Nuker", ModuleCategory.PLAYER);

    this.setRegistered(true);
    this.setDescription("Breaks blocks around you when holding the attack button. Requires Creative.");

    this.timeManager = new TimeManager();
  }

  @EventHandler
  public void onTick(IngameTickEvent e) {
    this.timeManager.updateTimer();

    if (Wrapper.getGameSettings().keyBindAttack.isKeyDown() && this.timeManager.sleep(150L)) {
      this.timeManager.updateLast();
      int range = 5;
      for (int x = -range; x < range; x++) {
        for (int y = -range; y < range; y++) {
          for (int z = -range; z < range; z++) {
            BlockPos blockPos = new BlockPos(Wrapper.getPlayer().posX + x, Wrapper.getPlayer().posY + y,
                    Wrapper.getPlayer().posZ + z);

            if (Wrapper.getWorld() != null && Block.getIdFromBlock(
                    Wrapper.getWorld().getBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ())) != 0) {
              Wrapper.getSendQueue()
                      .addToSendQueue(new C07PacketPlayerDigging(
                              C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos,
                              Wrapper.getPlayer().getHorizontalFacing()));
              Wrapper.getSendQueue().addToSendQueue(
                      new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                              blockPos, Wrapper.getPlayer().getHorizontalFacing()));
              Wrapper.getPlayer().swingItem();
            }
          }
        }
      }
    }
  }
}

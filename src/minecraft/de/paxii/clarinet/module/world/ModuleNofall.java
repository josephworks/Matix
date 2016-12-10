package de.paxii.clarinet.module.world;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.SendPacketEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

import net.minecraft.network.play.client.CPacketPlayer;

public class ModuleNofall extends Module {
  public ModuleNofall() {
    super("Nofall", ModuleCategory.WORLD);

    this.setRegistered(true);
    this.setDescription("Disables fall damage.");
  }

  @EventHandler
  public void onSendPacket(SendPacketEvent e) {
    if (!Wrapper.getPlayer().onGround && e.getPacket() instanceof CPacketPlayer) {
      CPacketPlayer curPacket = (CPacketPlayer) e.getPacket();
      curPacket.setOnGround(true);
      e.setPacket(curPacket);
    }
  }
}

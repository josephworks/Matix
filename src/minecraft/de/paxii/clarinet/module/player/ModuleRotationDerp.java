package de.paxii.clarinet.module.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.module.killaura.TimeManager;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Random;

public class ModuleRotationDerp extends Module {
  private final TimeManager timeManager;
  private final Random random;

  public ModuleRotationDerp() {
    super("RotationDerp", ModuleCategory.PLAYER, -1);

    this.setRegistered(true);
    this.setDescription("Spins your head. Only visible server-side.");

    this.timeManager = new TimeManager();
    this.random = new Random();
  }

  @EventHandler
  public void onTick(IngameTickEvent event) {
    timeManager.updateTimer();

    if (Wrapper.getPlayer().getHeldItem() != null) {
      ItemStack it = Wrapper.getPlayer().getHeldItem();

      if (it != null && it.getItem() instanceof ItemBow) {
        return;
      }
    }

    if (timeManager.sleep(200L)) {
      float derpYaw = random.nextInt(360);
      float derpPitch = random.nextInt(180);

      Wrapper.getSendQueue().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(derpYaw, derpPitch, Wrapper.getPlayer().onGround));

      timeManager.updateLast();
    }
  }
}

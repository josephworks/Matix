package de.paxii.clarinet.module.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.entity.EntityVelocityEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class ModuleAutoFish extends Module {

  private boolean fishSound;
  private ISoundEventListener soundEventListener;

  public ModuleAutoFish() {
    super("AutoFish", ModuleCategory.PLAYER, -1);

    this.setRegistered(true);
    this.setDescription("Automatically fishes for you.");
    this.soundEventListener = (soundIn, accessor) -> {
      if (soundIn.getSound().getSoundLocation().getResourcePath().equals("random/splash")) {
        this.fishSound = true;
      }
    };
  }

  @Override
  public void onEnable() {
    Wrapper.getMinecraft().getSoundHandler().sndManager.addListener(this.soundEventListener);
  }

  @EventHandler
  public void onEntityVelocity(EntityVelocityEvent event) {
    Entity entity = Wrapper.getWorld().getEntityByID(event.getEntityID());

    if (entity instanceof EntityFishHook) {
      EntityFishHook fishHook = (EntityFishHook) entity;

      if (this.fishSound
              && fishHook.angler.getEntityId() == Wrapper.getPlayer().getEntityId()
              && event.getVelocityPacket().getMotionX() == 0
              && event.getVelocityPacket().getMotionZ() == 0) {
        new Thread(() -> {
          for (int i = 0; i < 2; i++) {
            try {
              Thread.sleep(200);
            } catch (InterruptedException exception) {
              exception.printStackTrace();
            }

            Wrapper.getSendQueue().addToSendQueue(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
          }
        }).start();
        this.fishSound = false;
      }
    }
  }

  @Override
  public void onDisable() {
    Wrapper.getMinecraft().getSoundHandler().sndManager.removeListener(this.soundEventListener);
  }

}

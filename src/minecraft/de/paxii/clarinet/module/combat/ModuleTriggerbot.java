package de.paxii.clarinet.module.combat;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.settings.type.ClientSettingInteger;

import net.minecraft.entity.Entity;
import net.minecraft.src.Reflector;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;

public class ModuleTriggerbot extends Module {
  private final TimeManager timeManager;
  private MovingObjectPosition objectMouseOver;

  public ModuleTriggerbot() {
    super("Triggerbot", ModuleCategory.COMBAT);

    this.setCommand(true);
    this.setRegistered(true);
    this.setDescription("Automatically hits entities when you hover over them while holding the trigger key (default is left alt)");

    this.getModuleValues().put("clickSpeed", new ValueBase("Triggerbot Speed", 8.2F, 1F, 20F, "Speed"));
    this.getModuleValues().put("clickRange", new ValueBase("Triggerbot Range", 4.5F, 1F, 6.6F, "Range"));
    this.getModuleValues().put("randomness", new ValueBase(String.format("%s Random", this.getName()), 50.0F, 1.0F, 250.0F, true, "Randomness"));
    this.getModuleSettings().put("triggerKey", new ClientSettingInteger("Trigger Key", 56));

    this.timeManager = new TimeManager();
    this.timeManager.setRandom(true);
  }

  @EventHandler
  public void onTick(IngameTickEvent event) {
    if (this.isKeyDown()) {
      this.getMouseOver(Wrapper.getMinecraft().getTimer().renderPartialTicks);
      this.timeManager.updateTimer((int) this.getModuleValues().get("randomness").getValue());

      if (this.objectMouseOver != null && this.objectMouseOver.entityHit != null) {
        Entity pointedEntity = this.objectMouseOver.entityHit;

        if (shouldAttack(pointedEntity) && this.isDelayComplete()) {
          Wrapper.getMinecraft().playerController.attackEntity(Wrapper.getPlayer(), pointedEntity);
          Wrapper.getPlayer().swingItem();
          this.timeManager.updateLast();
        }
      }
    }
  }

  private boolean isDelayComplete() {
    return this.timeManager.sleep((long) (1000L / this.getModuleValues().get("clickSpeed").getValue()));
  }

  private boolean isKeyDown() {
    int triggerKey = this.getValueOrDefault("triggerKey", Integer.class, 56);
    return triggerKey >= 0 ? Keyboard.isKeyDown(triggerKey) : Mouse.isButtonDown(triggerKey + 100);
  }

  private boolean shouldAttack(Entity entity) {
    return !entity.isDead
            && Wrapper.getPlayer().getDistanceToEntity(entity) <= this
            .getModuleValues().get("clickRange").getValue()
            && Wrapper.getPlayer().canEntityBeSeen(entity)
            && !Wrapper.getFriendManager().isFriend(
            entity.getCommandSenderName());
  }

  private void getMouseOver(float partialTicks) {
    Entity entity = Wrapper.getMinecraft().getRenderViewEntity();
    Entity pointedEntity;

    if (entity != null && Wrapper.getWorld() != null) {
      double d0 = (double) this.getModuleValues().get("clickRange").getValue();
      double var3 = (double) Wrapper.getMinecraft().playerController.getBlockReachDistance();
      this.objectMouseOver = entity.rayTrace(var3, partialTicks);
      double var5 = var3;
      Vec3 var7 = entity.getPositionEyes(partialTicks);

      if (Wrapper.getMinecraft().playerController.extendedReach()) {
        var3 = 6.0D;
        var5 = 6.0D;
      } else {
        if (var3 > 3.0D) {
          var5 = 3.0D;
        }

        var3 = var5;
      }

      if (this.objectMouseOver != null) {
        var5 = this.objectMouseOver.hitVec.distanceTo(var7);
      }

      Vec3 var8 = entity.getLook(partialTicks);
      Vec3 var9 = var7.addVector(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3);
      pointedEntity = null;
      Vec3 var10 = null;
      float var11 = 1.0F;
      List var12 = Wrapper.getWorld().getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().addCoord(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3).expand((double) var11, (double) var11, (double) var11));
      double var13 = var5;

      for (int var15 = 0; var15 < var12.size(); ++var15) {
        Entity var16 = (Entity) var12.get(var15);

        if (var16.canBeCollidedWith()) {
          float var17 = var16.getCollisionBorderSize();
          AxisAlignedBB var18 = var16.getEntityBoundingBox().expand((double) var17, (double) var17, (double) var17);
          MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);

          if (var18.isVecInside(var7)) {
            if (0.0D < var13 || var13 == 0.0D) {
              pointedEntity = var16;
              var10 = var19 == null ? var7 : var19.hitVec;
              var13 = 0.0D;
            }
          } else if (var19 != null) {
            double var20 = var7.distanceTo(var19.hitVec);

            if (var20 < var13 || var13 == 0.0D) {
              boolean canRiderInteract = false;

              if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                canRiderInteract = Reflector.callBoolean(var16, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
              }

              if (var16 == entity.ridingEntity && !canRiderInteract) {
                if (var13 == 0.0D) {
                  pointedEntity = var16;
                  var10 = var19.hitVec;
                }
              } else {
                pointedEntity = var16;
                var10 = var19.hitVec;
                var13 = var20;
              }
            }
          }
        }
      }

      if (pointedEntity != null && (var13 < var5 || this.objectMouseOver == null)) {
        this.objectMouseOver = new MovingObjectPosition(pointedEntity, var10);
      }
    }
  }

  @Override
  public void onCommand(String[] args) {
    if (args.length >= 2) {
      if (args[0].equalsIgnoreCase("autospeed")) {
        try {
          boolean autoSpeed = Boolean.parseBoolean(args[1]);
          this.setValue("autoDelay", autoSpeed);

          Chat.printClientMessage("TriggerBot auto speed mode has been set to " + autoSpeed + ".");
        } catch (Exception e) {
          Chat.printClientMessage("Invalid argument!");
        }
      } else {
        Chat.printClientMessage("Invalid subcommand! Use \"" + ClientSettings.getValue("client.prefix", String.class) + "help triggerbot\"");
      }
    } else {
      Chat.printClientMessage("Too few arguments!");
    }
  }
}

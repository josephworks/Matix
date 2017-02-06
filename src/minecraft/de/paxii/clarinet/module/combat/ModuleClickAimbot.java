package de.paxii.clarinet.module.combat;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PlayerClickBlockEvent;
import de.paxii.clarinet.event.events.player.PostMotionUpdateEvent;
import de.paxii.clarinet.event.events.player.PreMotionUpdateEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.killaura.AuraManager;
import de.paxii.clarinet.util.module.killaura.EntityManager;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import de.paxii.clarinet.util.settings.ClientSettings;

import net.minecraft.entity.EntityLivingBase;

public class ModuleClickAimbot extends Module {
  private final AuraManager auraManager;
  private final TimeManager timeManager;
  private final EntityManager entityManager;
  private EntityLivingBase target;

  public ModuleClickAimbot() {
    super("ClickAimbot", ModuleCategory.COMBAT, -1);
    this.setCommand(true);
    this.setRegistered(true);
    this.setSyntax("clickaimbot set <speed/range/animals/mobs/players/silent/legit/autospeed> <digit/true/false>");
    this.setDescription("Aims and hits entities if you hold your attack button.");

    this.auraManager = new AuraManager(this);
    this.timeManager = new TimeManager();
    this.entityManager = new EntityManager(this.auraManager,
            Wrapper.getFriendManager());
  }

  @EventHandler
  public void preMotion(PreMotionUpdateEvent event) {
    if (this.auraManager.isSilent()) {
      this.auraManager.saveCamera(Wrapper.getPlayer());
    }

    if (!Wrapper.getMinecraft().gameSettings.keyBindAttack.isKeyDown()) {
      return;
    }

    this.timeManager.updateTimer();
    this.target = entityManager.getClosestEntityToCursor(this.auraManager.getAngle());

    if (this.target == null) {
      return;
    }

    this.auraManager.setAngles(this.target, this.entityManager);

    if (this.auraManager.isLegit() && !this.auraManager.isSilent() &&
            !(Wrapper.getMinecraft().pointedEntity != null &&
                    Wrapper.getMinecraft().pointedEntity.getEntityId() == this.target.getEntityId())) {
      return;
    }

    if (timeManager.sleep(this.auraManager.getDelay())) {
      Wrapper.getMinecraft().playerController.attackEntity(Wrapper.getPlayer(), this.target);
      Wrapper.getPlayer().swingItem();
      this.auraManager.addToAttackMap(this.target.getEntityId(), this.timeManager.getLast());
      this.timeManager.updateLast();
    }
  }

  @EventHandler
  public void postMotion(PostMotionUpdateEvent event) {
    if (this.auraManager.isSilent()) {
      this.auraManager.restoreCamera(Wrapper.getPlayer());
    }
  }

  @EventHandler
  public void onClickBlock(PlayerClickBlockEvent event) {
    if (this.target != null) {
      event.setCancelled(true);
    }
  }

  @Override
  public void onCommand(String[] args) {
    if (args.length > 0) {
      if (args[0].equalsIgnoreCase("set")) {
        if (args.length >= 3) {
          String identifier = args[1];
          String value = args[2];

          if (identifier.equalsIgnoreCase("speed")) {
            try {
              float speed = Float.parseFloat(value);
              this.auraManager.setDelay(speed);

              Chat.printClientMessage("ClickAimbot speed has been set to " + speed + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("range")) {
            try {
              float range = Float.parseFloat(value);
              this.auraManager.setDelay(range);

              Chat.printClientMessage("ClickAimbot range has been set to " + range + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("animals")) {
            try {
              boolean animal = Boolean.parseBoolean(value);
              this.auraManager.setAnimal(animal);

              Chat.printClientMessage("ClickAimbot animal mode has been set to " + animal + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("mobs")) {
            try {
              boolean mob = Boolean.parseBoolean(value);
              this.auraManager.setMob(mob);

              Chat.printClientMessage("ClickAimbot mob mode has been set to " + mob + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("players")) {
            try {
              boolean player = Boolean.parseBoolean(value);
              this.auraManager.setPlayer(player);

              Chat.printClientMessage("ClickAimbot player mode has been set to " + player + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("silent")) {
            try {
              boolean silent = Boolean.parseBoolean(value);
              this.auraManager.setSilent(silent);

              Chat.printClientMessage("ClickAimbot silent mode has been set to " + silent + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("legit")) {
            try {
              boolean legit = Boolean.parseBoolean(value);
              this.auraManager.setLegit(legit);

              Chat.printClientMessage("ClickAimbot legit mode has been set to " + legit + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else {
            Chat.printClientMessage("Invalid subcommand! Use \"" + ClientSettings.getValue("client.prefix", String.class) + "help aimbot\"");
          }
        } else {
          Chat.printClientMessage("Too few arguments!");
        }
      } else {
        Chat.printClientMessage("Invalid subcommand! Use \"" + ClientSettings.getValue("client.prefix", String.class) + "help aimbot\"");
      }
    } else {
      Chat.printClientMessage("Too few arguments!");
    }
  }
}

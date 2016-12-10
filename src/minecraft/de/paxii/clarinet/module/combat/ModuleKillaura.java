package de.paxii.clarinet.module.combat;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PostMotionUpdateEvent;
import de.paxii.clarinet.event.events.player.PreMotionUpdateEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.killaura.AuraManager;
import de.paxii.clarinet.util.module.killaura.EntityManager;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.settings.ClientSettings;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;

import org.lwjgl.input.Keyboard;

public class ModuleKillaura extends Module {
  private final AuraManager auraManager;
  private final EntityManager entityManager;
  private final TimeManager timeManager;
  private final ValueBase valueRandomness;
  private EntityLivingBase target;

  public ModuleKillaura() {
    super("KillAura", ModuleCategory.COMBAT, Keyboard.KEY_F);
    this.setCommand(true);
    this.setRegistered(true);
    this.setSyntax("killaura set <speed/range/animals/mobs/players/silent/legit/autospeed> <digit/true/false>");
    this.setDescription("Automatically hits entities around you.");

    this.auraManager = new AuraManager(this);
    this.timeManager = new TimeManager();
    this.timeManager.setRandom(true);
    this.entityManager = new EntityManager(this.auraManager,
            Wrapper.getFriendManager());
    this.valueRandomness = new ValueBase(String.format("%s Random", this.getName()), 50.0F, 1.0F, 250.0F, true, "Randomness");

    this.getModuleValues().put("randomness", valueRandomness);
  }

  @EventHandler
  public void preMotion(PreMotionUpdateEvent event) {
    if (this.auraManager.isSilent()) {
      this.auraManager.saveCamera(Wrapper.getPlayer());
    }

    this.timeManager.updateTimer((int) this.valueRandomness.getValue());
    this.target = entityManager.getEntity(this.auraManager.getAngle(), this.auraManager.getRange());

    if (this.target == null) {
      return;
    }

    this.auraManager.setAngles(this.target, this.entityManager);

    if (this.auraManager.isLegit() && !this.auraManager.isSilent() &&
            !(Wrapper.getMinecraft().pointedEntity != null &&
                    Wrapper.getMinecraft().pointedEntity.getEntityId() == this.target.getEntityId())) {
      return;
    }

    if (this.auraManager.isDelayComplete(this.timeManager)) {
      Wrapper.getMinecraft().playerController.attackEntity(Wrapper.getPlayer(), this.target);
      Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
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

              Chat.printClientMessage("KillAura speed has been set to " + speed + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("range")) {
            try {
              float range = Float.parseFloat(value);
              this.auraManager.setDelay(range);

              Chat.printClientMessage("KillAura range has been set to " + range + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("animals")) {
            try {
              boolean animal = Boolean.parseBoolean(value);
              this.auraManager.setAnimal(animal);

              Chat.printClientMessage("KillAura animal mode has been set to " + animal + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("mobs")) {
            try {
              boolean mob = Boolean.parseBoolean(value);
              this.auraManager.setMob(mob);

              Chat.printClientMessage("KillAura mob mode has been set to " + mob + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("players")) {
            try {
              boolean player = Boolean.parseBoolean(value);
              this.auraManager.setPlayer(player);

              Chat.printClientMessage("KillAura player mode has been set to " + player + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("silent")) {
            try {
              boolean silent = Boolean.parseBoolean(value);
              this.auraManager.setSilent(silent);

              Chat.printClientMessage("KillAura silent mode has been set to " + silent + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("legit")) {
            try {
              boolean legit = Boolean.parseBoolean(value);
              this.auraManager.setLegit(legit);

              Chat.printClientMessage("KillAura legit mode has been set to " + legit + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("invisible")) {
            try {
              boolean invisible = Boolean.parseBoolean(value);
              this.auraManager.setInvisible(invisible);

              Chat.printClientMessage("KillAura invisibility mode has been set to " + invisible + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else if (identifier.equalsIgnoreCase("autospeed")) {
            try {
              boolean autoSpeed = Boolean.parseBoolean(value);
              this.auraManager.setAutoSpeed(autoSpeed);

              Chat.printClientMessage("KillAura auto speed mode has been set to " + autoSpeed + ".");
            } catch (Exception e) {
              Chat.printClientMessage("Invalid argument!");
            }
          } else {
            Chat.printClientMessage("Invalid subcommand! Use \"" + ClientSettings.getValue("client.prefix", String.class) + "help killaura\"");
          }
        } else {
          Chat.printClientMessage("Too few arguments!");
        }
      } else {
        Chat.printClientMessage("Invalid subcommand! Use \"" + ClientSettings.getValue("client.prefix", String.class) + "help killaura\"");
      }
    } else {
      Chat.printClientMessage("Too few arguments!");
    }
  }
}

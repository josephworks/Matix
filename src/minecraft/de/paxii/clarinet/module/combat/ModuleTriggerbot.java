package de.paxii.clarinet.module.combat;

import com.google.common.base.Predicates;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.settings.type.ClientSettingBoolean;
import de.paxii.clarinet.util.settings.type.ClientSettingInteger;
import net.minecraft.entity.Entity;
import net.minecraft.src.Reflector;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;

public class ModuleTriggerbot extends Module {
	private final TimeManager timeManager;
	private RayTraceResult objectMouseOver;

	public ModuleTriggerbot() {
		super("Triggerbot", ModuleCategory.COMBAT);

		this.setCommand(true);
		this.setRegistered(true);
		this.setDescription("Automatically hits entities when you hover over them while holding the trigger key (default is left alt)");
		this.setSyntax("triggerbot autospeed <true/false>");

		this.getModuleValues().put("clickSpeed", new ValueBase("Triggerbot Speed", 8.2F, 1F, 20F, "Speed"));
		this.getModuleValues().put("clickRange", new ValueBase("Triggerbot Range", 4.5F, 1F, 6.6F, "Range"));
		this.getModuleValues().put("randomness", new ValueBase(String.format("%s Random", this.getName()), 50.0F, 1.0F, 250.0F, true, "Randomness"));
		this.getModuleSettings().put("triggerKey", new ClientSettingInteger("Trigger Key", 56));
		this.getModuleSettings().put("autoDelay", new ClientSettingBoolean("Auto Delay", false));

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
					Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
					this.timeManager.updateLast();
				}
			}
		}
	}

	private boolean isDelayComplete() {
		if (this.getValueOrDefault("autoDelay", Boolean.class, false)) {
			return Wrapper.getPlayer().getCooledAttackStrength(0.0F) >= 1.0F;
		}

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
				entity.getName());
	}

	private void getMouseOver(float partialTicks) {
		Entity entity = Wrapper.getMinecraft().getRenderViewEntity();
		Entity pointedEntity;

		if (entity != null && Wrapper.getWorld() != null) {
			double d0 = (double) this.getModuleValues().get("clickRange").getValue();
			this.objectMouseOver = entity.rayTrace(d0, partialTicks);
			double d1 = d0;
			Vec3d vec3d = entity.getPositionEyes(partialTicks);

			if (this.objectMouseOver != null) {
				d1 = this.objectMouseOver.hitVec.distanceTo(vec3d);
			}

			Vec3d vec3d1 = entity.getLook(partialTicks);
			Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * d0, vec3d1.yCoord * d0, vec3d1.zCoord * d0);
			pointedEntity = null;
			Vec3d vec3d3 = null;
			float f = 1.0F;
			List<Entity> list = Wrapper.getWorld().getEntitiesInAABBexcluding(
					entity,
					entity.getEntityBoundingBox().addCoord(
							vec3d1.xCoord * d0,
							vec3d1.yCoord * d0,
							vec3d1.zCoord * d0).expand(
							(double) f,
							(double) f,
							(double) f),
					Predicates.and(EntitySelectors.NOT_SPECTATING, (p_apply_1_) -> p_apply_1_ != null && p_apply_1_.canBeCollidedWith()));

			double d2 = d1;

			for (Entity entity1 : list) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz((double) entity1.getCollisionBorderSize());
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

				if (axisalignedbb.isVecInside(vec3d)) {
					if (d2 >= 0.0D) {
						pointedEntity = entity1;
						vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
						d2 = 0.0D;
					}
				} else if (raytraceresult != null) {
					double d3 = vec3d.distanceTo(raytraceresult.hitVec);

					if (d3 < d2 || d2 == 0.0D) {
						boolean flag1 = false;

						if (Reflector.ForgeEntity_canRiderInteract.exists()) {
							flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract);
						}

						if (!flag1 && entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
							if (d2 == 0.0D) {
								pointedEntity = entity1;
								vec3d3 = raytraceresult.hitVec;
							}
						} else {
							pointedEntity = entity1;
							vec3d3 = raytraceresult.hitVec;
							d2 = d3;
						}
					}
				}
			}

			if (pointedEntity != null && (d2 < d1 || this.objectMouseOver == null)) {
				this.objectMouseOver = new RayTraceResult(pointedEntity, vec3d3);
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

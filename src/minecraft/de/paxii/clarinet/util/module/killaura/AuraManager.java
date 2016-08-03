package de.paxii.clarinet.util.module.killaura;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.settings.type.ClientSettingBoolean;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AuraManager {
	private final Module module;

	private final ValueBase valueRange;
	private final ValueBase valueDelay;
	private final ValueBase valueAngle;

	private final TimeManager timeManager;

	private final ArrayList<Float> pitchMap;
	private final ArrayList<Float> yawMap;

	private Map<Integer, Long> attackMap;
	private final CameraObject cameraObject;

	private final Random random;

	public AuraManager(Module module) {
		this.module = module;
		this.attackMap = new HashMap<>();
		this.cameraObject = new CameraObject();
		this.pitchMap = new ArrayList<>();
		this.yawMap = new ArrayList<>();
		this.random = new Random();

		this.timeManager = new TimeManager();

		this.valueRange = new ValueBase(module.getName() + " Range", 4.1F, 1, 6);
		this.valueDelay = new ValueBase(module.getName() + " Speed", 2.0F, 1, 15) {
			@Override
			public void onUpdate(float oldValue, float newValue) {
				if (AuraManager.this.isAutoSpeed()) {
					AuraManager.this.setAutoSpeed(false);
					Chat.printClientMessage(String.format("Disabled auto speed for %s.", module.getName()));
				}
			}
		};
		this.valueAngle = new ValueBase(module.getName() + " Angle", 80.0F, 1.0F, 180.0F);

		this.module.getModuleValues().put("valueRange", this.valueRange);
		this.module.getModuleValues().put("valueDelay", this.valueDelay);
		this.module.getModuleValues().put("valueAngle", this.valueAngle);

		this.module.getModuleSettings().put("animal", new ClientSettingBoolean("Animals", false));
		this.module.getModuleSettings().put("mob", new ClientSettingBoolean("Mobs", true));
		this.module.getModuleSettings().put("player", new ClientSettingBoolean("Players", true));
		this.module.getModuleSettings().put("silent", new ClientSettingBoolean("Silent", true));
		this.module.getModuleSettings().put("legit", new ClientSettingBoolean("Legit", true));
		this.module.getModuleSettings().put("invisible", new ClientSettingBoolean("Invisible", false));
		this.module.getModuleSettings().put("autoDelay", new ClientSettingBoolean("Auto Delay", false));

		for (float f = -10.0F; f <= 10.0F; f += 2.0F) {
			this.yawMap.add(f);
		}

		for (float f = -10.0F; f <= 50.0F; f += 5.0F) {
			this.pitchMap.add(f);
		}
	}

	public void addToAttackMap(final int entityId, final long last) {
		attackMap.put(entityId, last);
	}

	private long getLast(Entity entity) {
		if (attackMap.get(entity.getEntityId()) == null) {
			return 0L;
		}

		return attackMap.get(entity.getEntityId());
	}

	public long getDelay() {
		return (long) (1000L / this.valueDelay.getValue());
	}

	public void setDelay(final float delay) {
		this.valueDelay.setValue(delay);
	}

	public boolean isDelayComplete(TimeManager timeManager) {
		if (this.isAutoSpeed()) {
			return Wrapper.getPlayer().getCooledAttackStrength(0.0F) >= 1.0F;
		}

		return timeManager.sleep(this.getDelay());
	}

	public double getRange() {
		return this.valueRange.getValue();
	}

	public void setRange(final float range) {
		this.valueRange.setValue(range);
	}

	public float getAngle() {
		return this.valueAngle.getValue();
	}

	public Map<Integer, Long> getAttackMap() {
		return attackMap;
	}

	public void setAttackMap(Map<Integer, Long> attackMap) {
		this.attackMap = attackMap;
	}

	public long convertToMilliseconds(double delay) {
		return 1000L / (long) delay;
	}

	public void saveCamera(EntityPlayer p) {
		this.cameraObject.saveCamera(p);
	}

	public void restoreCamera(EntityPlayer p) {
		this.cameraObject.restoreCamera(p);
	}

	public float getRandomPitchModifier() {
		int randomIndex = random.nextInt(pitchMap.size() - 1);

		return pitchMap.get(randomIndex);
	}

	public float getRandomYawModifier() {
		int randomIndex = random.nextInt(yawMap.size() - 1);

		return yawMap.get(randomIndex);
	}

	public boolean isSilent() {
		return this.module.getValue("silent", Boolean.class);
	}

	public void setSilent(boolean silent) {
		this.module.setValue("silent", silent);
	}

	public boolean isMob() {
		return this.module.getValue("mob", Boolean.class);
	}

	public void setMob(boolean mob) {
		this.module.setValue("silent", mob);
	}

	public boolean isAnimal() {
		return this.module.getValue("animal", Boolean.class);
	}

	public void setAnimal(boolean animal) {
		this.module.setValue("animal", animal);
	}

	public boolean isPlayer() {
		return this.module.getValue("player", Boolean.class);
	}

	public void setPlayer(boolean player) {
		this.module.setValue("player", player);
	}

	public boolean isLegit() {
		return this.module.getValue("legit", Boolean.class);
	}

	public void setLegit(boolean legit) {
		this.module.setValue("legit", legit);
	}

	public boolean isInvisible() {
		return this.module.getValue("invisible", Boolean.class);
	}

	public void setInvisible(boolean invisible) {
		this.module.setValue("invisible", invisible);
	}

	public boolean isAutoSpeed() {
		return this.module.getValue("autoDelay", Boolean.class);
	}

	public void setAutoSpeed(boolean autoSpeed) {
		this.module.setValue("autoDelay", autoSpeed);
	}

	public void setAngles(EntityLivingBase entityLiving, EntityManager entityManager) {
		float yaw = entityManager.getAngles(entityLiving)[0];
		float pitch = entityManager.getAngles(entityLiving)[1];

		if (this.isLegit()) {
			this.timeManager.updateTimer();

			if (this.timeManager.sleep(200L)) {
				this.setPitch(pitch + (random.nextFloat() - 0.5F) * 50F);
				this.setYaw(yaw + (random.nextFloat() - 0.5F) * 10F);

				this.timeManager.updateLast();
			} else {
				this.setPitch(pitch);
				this.setYaw(yaw);
			}
		} else {
			this.setPitch(pitch);
			this.setYaw(yaw);
		}
	}

	private void setYaw(float yaw) {
		Wrapper.getPlayer().rotationYaw = yaw;
		Wrapper.getPlayer().rotationYawHead = yaw;
	}

	private void setPitch(float pitch) {
		Wrapper.getPlayer().rotationPitch = pitch;
	}
}

class CameraObject {
	private float cameraYaw;
	private float cameraPitch;
	private float cameraYawHead;

	public void saveCamera(EntityPlayer p) {
		this.cameraYaw = p.rotationYaw;
		this.cameraPitch = p.rotationPitch;
		this.cameraYawHead = p.rotationYawHead;
	}

	public void restoreCamera(EntityPlayer p) {
		p.rotationYaw = this.cameraYaw;
		p.rotationPitch = this.cameraPitch;
		p.rotationYawHead = this.cameraYawHead;
	}
}

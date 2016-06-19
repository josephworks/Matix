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
	private Module module;

	private ValueBase valueRange;
	private ValueBase valueDelay;
	private ValueBase valueAngle;

	@Getter
	@Setter
	private boolean silent = true, mob = true, animal = false, player = true, legit = true, invisible = false, autoSpeed = false;

	private TimeManager timeManager;

	private ArrayList<Float> pitchMap;
	private ArrayList<Float> yawMap;

	private Map<Integer, Long> attackMap;
	private CameraObject cameraObject;

	private Random random;

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

		this.module.getModuleSettings().put("animal", new ClientSettingBoolean("animal", this.isAnimal()));
		this.module.getModuleSettings().put("mob", new ClientSettingBoolean("mob", this.isMob()));
		this.module.getModuleSettings().put("player", new ClientSettingBoolean("player", this.isPlayer()));
		this.module.getModuleSettings().put("silent", new ClientSettingBoolean("silent", this.isSilent()));
		this.module.getModuleSettings().put("legit", new ClientSettingBoolean("legit", this.isLegit()));
		this.module.getModuleSettings().put("invisible", new ClientSettingBoolean("invisible", this.isInvisible()));
		this.module.getModuleSettings().put("autoDelay", new ClientSettingBoolean("autoDelay", this.isAutoSpeed()));

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

	public void onStartUp() {
		this.setAnimal(this.module.getValueOrDefault("animal", Boolean.class, true));
		this.setMob(this.module.getValueOrDefault("mob", Boolean.class, true));
		this.setPlayer(this.module.getValueOrDefault("player", Boolean.class, true));
		this.setSilent(this.module.getValueOrDefault("silent", Boolean.class, true));
		this.setLegit(this.module.getValueOrDefault("legit", Boolean.class, true));
		this.setInvisible(this.module.getValueOrDefault("invisible", Boolean.class, false));
		this.setAutoSpeed(this.module.getValueOrDefault("autoDelay", Boolean.class, false));
	}

	public void onShutdown() {
		this.module.getModuleSettings().put("animal", new ClientSettingBoolean("animal", this.isAnimal()));
		this.module.getModuleSettings().put("mob", new ClientSettingBoolean("mob", this.isMob()));
		this.module.getModuleSettings().put("player", new ClientSettingBoolean("player", this.isPlayer()));
		this.module.getModuleSettings().put("silent", new ClientSettingBoolean("silent", this.isSilent()));
		this.module.getModuleSettings().put("legit", new ClientSettingBoolean("legit", this.isLegit()));
		this.module.getModuleSettings().put("invisible", new ClientSettingBoolean("invisible", this.isInvisible()));
		this.module.getModuleSettings().put("autoDelay", new ClientSettingBoolean("autoDelay", this.isAutoSpeed()));
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

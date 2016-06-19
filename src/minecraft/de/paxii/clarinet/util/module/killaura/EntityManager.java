package de.paxii.clarinet.util.module.killaura;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.module.friends.FriendManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class EntityManager {
	private final AuraManager auraManager;
	private FriendManager friends;

	public EntityManager(final AuraManager auraManager, FriendManager friends) {
		this.auraManager = auraManager;
		this.friends = friends;
	}

	public EntityLivingBase getClosestEntity(final double range) {
		double distance = range;
		EntityLivingBase tempEntity = null;


		for (Entity entity : Wrapper.getWorld().loadedEntityList) {
			if (!(entity instanceof EntityLivingBase)) {
				continue;
			}

			final EntityLivingBase living = (EntityLivingBase) entity;

			if (!canAttackEntity(living)) {
				continue;
			}

			final double curDistance = Wrapper.getPlayer().getDistanceToEntity(
					living);

			if (curDistance <= distance) {
				distance = curDistance;
				tempEntity = living;
			}
		}

		return tempEntity;
	}

	public EntityLivingBase getClosestEntityToCursor(final float angle) {
		float distance = angle;
		EntityLivingBase tempEntity = null;

		for (Entity entity : Wrapper.getWorld().loadedEntityList) {
			if (!(entity instanceof EntityLivingBase)) {
				continue;
			}

			final EntityLivingBase living = (EntityLivingBase) entity;

			if (!canAttackEntity(living)) {
				continue;
			}

			final float[] angles = getAngles(living);
			final float yaw = getDistanceBetweenAngles(
					Wrapper.getPlayer().rotationYawHead, angles[0]);
			final float pitch = getDistanceBetweenAngles(
					Wrapper.getPlayer().rotationPitch, angles[1]);

			if (yaw > angle || pitch > angle) {
				continue;
			}

			final float curDistance = (yaw + pitch) / 2F;

			if (curDistance <= distance) {
				distance = curDistance;
				tempEntity = living;
			}
		}

		return tempEntity;
	}

	public EntityLivingBase getEntity(final float angle, final double distance) {
		final EntityLivingBase distanceCheck = getClosestEntity(distance);
		final EntityLivingBase angleCheck = getClosestEntityToCursor(angle);

		if (this.auraManager.isLegit()) {
			if (angleCheck == null)
				return null;
		}

		return angleCheck != null ? angleCheck : distanceCheck;
	}

	public float getDistanceBetweenAngles(final float ang1, final float ang2) {
		return Math.abs(((ang1 - ang2 + 180) % 360 + 360) % 360 - 180);
	}

	public boolean canAttackEntity(final EntityLivingBase entity) {
		return entity != Wrapper.getPlayer() && shouldAttack(entity);
	}

	private boolean shouldAttack(EntityLivingBase entityLiving) {
		if (entityLiving.deathTime > 0 || !entityLiving.isEntityAlive()) {
			return false;
		}

		if (!(Wrapper.getPlayer().canEntityBeSeen(entityLiving))) {
			return false;
		}

		if (Wrapper.getPlayer().getDistanceToEntity(entityLiving) > auraManager
				.getRange()) {
			return false;
		}

		if (!entityLiving.canBePushed()) {
			return false;
		}

		if (entityLiving instanceof EntityPlayer && !auraManager.isPlayer()) {
			return false;
		}

		if (entityLiving instanceof EntityPlayer && friends.isFriend(entityLiving.getName())) {
			return false;
		}

		if (entityLiving instanceof EntityAnimal && !auraManager.isAnimal()) {
			return false;
		}

		if (entityLiving instanceof EntityMob && !auraManager.isMob()) {
			return false;
		}

		if (entityLiving.isInvisible() && !auraManager.isInvisible()) {
			return false;
		}

		if (entityLiving.ticksExisted < 20) {
			return false;
		}

		if (this.auraManager.isLegit()
				&& entityLiving.onGround
				&& (entityLiving.posY - Wrapper.getPlayer().posY <= -2
				|| Wrapper.getPlayer().posY - entityLiving.posY >= 2)
				) {
			return false;
		}

		return true;
	}

	public float[] getAngles(final EntityLivingBase entityLiving) {
		double difX = entityLiving.posX - Wrapper.getPlayer().posX, difY = (entityLiving.posY + entityLiving
				.getEyeHeight())
				- (Wrapper.getPlayer().posY + Wrapper.getPlayer()
				.getEyeHeight()), difZ = entityLiving.posZ
				- Wrapper.getPlayer().posZ;
		double helper = Math.sqrt(difX * difX + difZ * difZ);
		float yaw = (float) (Math.atan2(difZ, difX) * 180 / Math.PI) - 90;
		float pitch = (float) -(Math.atan2(difY, helper) * 180 / Math.PI);
		return (new float[]{ yaw, pitch });
	}
}

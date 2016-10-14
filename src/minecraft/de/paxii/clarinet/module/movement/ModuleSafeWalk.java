package de.paxii.clarinet.module.movement;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.entity.EntityMoveEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

public class ModuleSafeWalk extends Module {
	public ModuleSafeWalk() {
		super("Safewalk", ModuleCategory.MOVEMENT);

		this.setRegistered(true);
		this.setDescription("Prevents you from falling off of edges. (like you're sneaking)");
	}

	@EventHandler
	public void onMoveEntity(EntityMoveEvent event) {
		double var13 = event.getX();
		double var15 = event.getY();
		double var17 = event.getZ();
		boolean var19 = Wrapper.getPlayer().onGround;

		if (var19) {
			double var20;

			for (var20 = 0.05D; event.getX() != 0.0D
					&& Wrapper
					.getWorld()
					.getCollidingBoundingBoxes(
							Wrapper.getPlayer(),
							Wrapper.getPlayer().getEntityBoundingBox()
							       .offset(event.getX(), -1.0D, 0.0D))
					.isEmpty(); var13 = event.getX()) {
				if (event.getX() < var20 && event.getX() >= -var20) {
					event.setX(0.0D);
				} else if (event.getX() > 0.0D) {
					event.setX(event.getX() - var20);
				} else {
					event.setX(event.getX() + var20);
				}
			}

			for (; event.getZ() != 0.0D
					&& Wrapper
					.getWorld()
					.getCollidingBoundingBoxes(
							Wrapper.getPlayer(),
							Wrapper.getPlayer().getEntityBoundingBox()
							       .offset(0.0D, -1.0D, event.getZ()))
					.isEmpty(); var17 = event.getZ()) {
				if (event.getZ() < var20 && event.getZ() >= -var20) {
					event.setZ(0.0D);
				} else if (event.getZ() > 0.0D) {
					event.setZ(event.getZ() - var20);
				} else {
					event.setZ(event.getZ() + var20);
				}
			}

			for (; event.getX() != 0.0D
					&& event.getZ() != 0.0D
					&& Wrapper
					.getWorld()
					.getCollidingBoundingBoxes(
							Wrapper.getPlayer(),
							Wrapper.getPlayer()
							       .getEntityBoundingBox()
							       .offset(event.getX(), -1.0D,
									       event.getZ())).isEmpty(); var17 = event
					.getZ()) {
				if (event.getX() < var20 && event.getX() >= -var20) {
					event.setX(0.0D);
				} else if (event.getX() > 0.0D) {
					event.setX(event.getX() - var20);
				} else {
					event.setX(event.getX() + var20);
				}

				var13 = event.getX();

				if (event.getZ() < var20 && event.getZ() >= -var20) {
					event.setZ(0.0D);
				} else if (event.getZ() > 0.0D) {
					event.setZ(event.getZ() - var20);
				} else {
					event.setZ(event.getZ() + var20);
				}
			}
		}
	}
}

package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.RenderTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.render.GL11Helper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ModuleProjectiles extends Module {
	public ModuleProjectiles() {
		super("Projectiles", ModuleCategory.RENDER, -1);

		this.setRegistered(true);
		this.setDescription("Predicts where projectiles will land. (probably not going to be correct)");
	}

	@EventHandler
	public void onRenderTick(RenderTickEvent event) {
		draw();
	}

	private void draw() {
		ItemStack is = Wrapper.getPlayer().inventory
				.getCurrentItem();

		if (is != null) {
			int id = Item.getIdFromItem(is.getItem());
			boolean drawPrediction = true;
			float var5 = 3.2F;
			float var6 = 0.0F;

			if (id == 261) {
				var5 = 5.4F;
			} else if (id == 368) {
				var5 = 1.5F;
			} else if (id == 332) {
				var5 = 1.5F;
			} else if (id == 346) {
				var5 = 1.5F;
			} else if (id == 344) {
				var5 = 1.5F;
			} else if ((is.getItemDamage() & 0x4000) > 0) {
				var5 = 0.5F;
				var6 = -20F;
			} else {
				drawPrediction = false;
			}

			if (drawPrediction) {
				AxisAlignedBB var9;
				float var10 = (float) Wrapper.getPlayer().posX;
				float var11 = (float) Wrapper.getPlayer().getEntityBoundingBox().minY + 1.5F;
				float var12 = (float) Wrapper.getPlayer().posZ;
				float var13 = Wrapper.getPlayer().rotationYaw;
				float var14 = Wrapper.getPlayer().rotationPitch;
				float var15 = var13 / 180.0F * (float) Math.PI;
				float var16 = MathHelper.sin(var15);
				float var17 = MathHelper.cos(var15);
				float var18 = var14 / 180.0F * (float) Math.PI;
				float var19 = MathHelper.cos(var18);
				float var20 = (var14 + var6) / 180.0F * (float) Math.PI;
				float var21 = MathHelper.sin(var20);
				float var22 = var10 - var17 * 0.16F;
				float var23 = var11 - 0.1F;
				float var24 = var12 - var16 * 0.16F;
				float var25 = -var16 * var19 * 0.4F;
				float var26 = -var21 * 0.4F;
				float var27 = var17 * var19 * 0.4F;
				float var28 = MathHelper.sqrt_double(var25 * var25 + var26
						* var26 + var27 * var27);
				var25 /= var28;
				var26 /= var28;
				var27 /= var28;
				var25 *= var5;
				var26 *= var5;
				var27 *= var5;
				float var29 = var22;
				float var30 = var23;
				float var31 = var24;
				GL11.glPushMatrix();
				GL11.glColor4f(255F, 0F, 0F, 1.0F);
				GL11Helper.enableDefaults();
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(false);
				GlStateManager.depthMask(false);
				GL11.glBegin(GL11.GL_LINES);
				int var32 = 0;
				boolean var33 = true;

				while (var33) {
					++var32;
					var29 += var25;
					var30 += var26;
					var31 += var27;
					Vec3d var34 = new Vec3d(var22, var23, var24);
					Vec3d var35 = new Vec3d(var29, var30, var31);
					RayTraceResult var8 = Wrapper.getWorld()
					                             .rayTraceBlocks(var34, var35);

					if (var8 != null) {
						var22 = (float) var8.hitVec.xCoord;
						var23 = (float) var8.hitVec.yCoord;
						var24 = (float) var8.hitVec.zCoord;

						var33 = false;
					} else if (var32 > 200) {
						var33 = false;
					} else {
						drawLine3D(var22 - var10, var23 - var11, var24 - var12,
								var29 - var10, var30 - var11, var31 - var12);

						var9 = new AxisAlignedBB(var22 - 0.125F, var23,
								var24 - 0.125F, var22 + 0.125F,
								var23 + 0.25F, var24 + 0.125F);

						float var4 = 0.0F;
						float var37;
						for (int var36 = 0; var36 < 5; ++var36) {
							var37 = (float) (var9.minY + (var9.maxY - var9.minY)
									* (var36) / 5.0D);
							float var38 = (float) (var9.minY + (var9.maxY - var9.minY)
									* (var36 + 1) / 5.0D);
							AxisAlignedBB var39 = new AxisAlignedBB(
									var9.minX, var37, var9.minZ, var9.maxX,
									var38, var9.maxZ);
							if (Wrapper.getWorld()
									.isAABBInMaterial(var39, Material.WATER)) {
								var4 += 0.2F;
							}
						}

						float var40 = var4 * 2.0F - 1.0F;
						var26 += 0.04F * var40;
						var37 = 0.92F;

						if (var4 > 0.0F) {
							var37 *= 0.9F;
							var26 *= 0.8F;
						}

						var25 *= var37;
						var26 *= var37;
						var27 *= var37;

						var22 = var29;
						var23 = var30;
						var24 = var31;
					}
				}

				GL11.glEnd();
				GlStateManager.depthMask(true);
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11Helper.disableDefaults();
				GL11.glPopMatrix();
			}
		}
	}

	private void drawLine3D(float var1, float var2, float var3, float var4,
	                        float var5, float var6) {
		GL11.glVertex3d(var1, var2, var3);
		GL11.glVertex3d(var4, var5, var6);
	}
}

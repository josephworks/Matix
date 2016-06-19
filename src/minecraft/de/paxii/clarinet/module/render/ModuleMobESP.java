package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.entity.PreRenderEntityEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.render.GL11Helper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class ModuleMobESP extends Module {
	public ModuleMobESP() {
		super("MobESP", ModuleCategory.RENDER, -1);

		this.setDescription("Draws a purple box around mobs and animals.");
	}

	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void renderESP(PreRenderEntityEvent event) {
		Entity entity = event.getRenderedEntity();

		if (!(entity instanceof EntityPlayer)) {
			if (entity instanceof EntityLiving) {
				this.renderMobESP(entity, event.getX(), event.getY(),
						event.getZ(), event.getRenderPartialTicks());
			}
		}
	}

	private void renderMobESP(Entity entityIn, double x, double y, double z,
	                          float partialTicks) {

		if (entityIn != Wrapper.getPlayer() && Wrapper.getWorld() != null) {
			GL11.glPushMatrix();
			GL11Helper.enableDefaults();
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(false);
			GlStateManager.depthMask(false);
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GL11.glLineWidth(1.0F);

			int boxColor = 0xFF00FF;

			AxisAlignedBB var11 = entityIn.getEntityBoundingBox();
			AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entityIn.posX
					+ x - 0.2D, var11.minY - entityIn.posY + y, var11.minZ
					- entityIn.posZ + z - 0.2D, var11.maxX - entityIn.posX + x
					+ 0.2D, var11.maxY - entityIn.posY + y + 0.2D, var11.maxZ
					- entityIn.posZ + z + 0.2D);

			RenderGlobal.drawOutlinedBoundingBox(var12, 201, 4, 155, 255);

			GL11Helper.disableDefaults();
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		}

	}

	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class ModulePlayerESP extends Module {
  public ModulePlayerESP() {
    super("PlayerESP", ModuleCategory.RENDER, -1);

    this.setRegistered(true);
    this.setDescription("Draws a box around players.");
  }

  @EventHandler
  public void renderESP(PreRenderEntityEvent event) {
    Entity entity = event.getRenderedEntity();

    if (entity instanceof EntityPlayer && entity != Wrapper.getPlayer()) {
      this.renderPlayerESP(entity, event.getX(), event.getY(),
              event.getZ(), event.getRenderPartialTicks());
    }
  }

  private void renderPlayerESP(Entity entityIn, double x, double y, double z,
                               float partialTicks) {
    if (Wrapper.getWorld() != null) {
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

      int boxColor = 0xFF0000;
      if (Wrapper.getFriendManager().isFriend(
              entityIn.getName())) {
        boxColor = Wrapper.getFriendManager().getFriendColor(entityIn.getName());
      }

      AxisAlignedBB var11 = entityIn.getEntityBoundingBox();
      AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entityIn.posX
              + x - 0.2D, var11.minY - entityIn.posY + y, var11.minZ
              - entityIn.posZ + z - 0.2D, var11.maxX - entityIn.posX + x
              + 0.2D, var11.maxY - entityIn.posY + y + 0.2D, var11.maxZ
              - entityIn.posZ + z + 0.2D);

      RenderGlobal.drawOutlinedBoundingBox(var12, boxColor);

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
}

package de.paxii.clarinet.module.render;

import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.entity.PostRenderEntityEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.render.GL11Helper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class ModuleMobESP extends Module {

  public ModuleMobESP() {
    super("MobESP", ModuleCategory.RENDER, -1);

    this.setRegistered(true);
    this.setDescription("Draws a purple box around mobs and animals.");
  }

  @EventHandler
  public void renderESP(PostRenderEntityEvent event) {
    Entity entity = event.getRenderedEntity();

    if (!(entity instanceof EntityPlayer) && entity instanceof EntityLiving) {
      GL11.glPushMatrix();
      GL11Helper.enableDefaults();
      GlStateManager.depthMask(false);
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.disableBlend();
      GL11.glLineWidth(1.0F);

      AxisAlignedBB var11 = entity.getEntityBoundingBox();
      AxisAlignedBB var12 = new AxisAlignedBB(
              var11.minX - entity.posX + event.getX() - 0.2D,
              var11.minY - entity.posY + event.getY(),
              var11.minZ - entity.posZ + event.getZ() - 0.2D,
              var11.maxX - entity.posX + event.getX() + 0.2D,
              var11.maxY - entity.posY + event.getY() + 0.2D,
              var11.maxZ - entity.posZ + event.getZ() + 0.2D
      );
      RenderGlobal.drawOutlinedBoundingBox(var12, 0xFF00FF);

      GL11Helper.disableDefaults();
      GlStateManager.enableTexture2D();
      GlStateManager.enableLighting();
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
      GL11.glPopMatrix();
    }
  }

}

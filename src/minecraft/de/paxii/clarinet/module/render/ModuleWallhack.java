package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.entity.PostRenderEntityEvent;
import de.paxii.clarinet.event.events.entity.PreRenderEntityEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;

import org.lwjgl.opengl.GL11;

public class ModuleWallhack extends Module {
  public ModuleWallhack() {
    super("Wallhack", ModuleCategory.RENDER, -1);

    this.setRegistered(true);
    this.setDescription("Renders entities through walls.");
  }

  @EventHandler
  public void preRender(PreRenderEntityEvent event) {
    if (event.getRenderedEntity() instanceof EntityLiving) {
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      Wrapper.getRenderer().disableLightmap();
    }
  }

  @EventHandler
  public void postRender(PostRenderEntityEvent event) {
    if (event.getRenderedEntity() instanceof EntityLiving) {
      Wrapper.getRenderer().enableLightmap();
      GlStateManager.enableDepth();
      GlStateManager.depthMask(true);
    }
  }
}

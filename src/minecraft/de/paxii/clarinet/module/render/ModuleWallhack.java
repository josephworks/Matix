package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.entity.PostRenderEntityEvent;
import de.paxii.clarinet.event.events.entity.PreRenderEntityEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ModuleWallhack extends Module {
	public ModuleWallhack() {
		super("Wallhack", ModuleCategory.RENDER, -1);

		this.setRegistered(true);
		this.setDescription("Renders entities through walls.");
	}

	@EventHandler
	public void preRender(PreRenderEntityEvent event) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GlStateManager.depthMask(false);
		Wrapper.getRenderer().disableLightmap();
	}

	@EventHandler
	public void postRender(PostRenderEntityEvent event) {
		Wrapper.getRenderer().enableLightmap();
		GlStateManager.depthMask(true);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}

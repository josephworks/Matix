package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.ChatColor;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.settings.type.ClientSettingBoolean;
import lombok.Getter;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * Created by Lars on 19.04.2016.
 */
public class ModuleNameTags extends Module {
	private static ModuleNameTags instance;
	@Getter
	private static boolean isActive;

	public ModuleNameTags() {
		super("NameTags", ModuleCategory.RENDER);

		ModuleNameTags.instance = this;
		this.setDescription("Renders bigger nametags alongside the health of other players");

		this.getModuleSettings().put("displayHealth", new ClientSettingBoolean("Health", true));
		this.getModuleValues().put("scale", new ValueBase("NameTag Scale", 3.0F, 1.0F, 10.0F));
	}

	@Override
	public void onEnable() {
		ModuleNameTags.isActive = true;
	}

	public static void drawHealthTags(Entity entity, FontRenderer fontRenderer, String nameTag, float posX, float posY, float posZ, int yOffset, float playerViewY, float playerViewX, boolean thirdPersonView, boolean isSneaking) {
		final double scale = Wrapper.getPlayer().getDistanceToEntity(entity) / instance.getModuleValues().get("scale").getValue();
		yOffset -= scale / 2;

		if (entity instanceof EntityOtherPlayerMP && instance.getValueOrDefault("scale", Boolean.class, true)) {
			int health = ((int) ((EntityOtherPlayerMP) entity).getHealth());
			nameTag += " | HP: " + (health >= 10 ? ChatColor.GREEN : ChatColor.RED) + health;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) (thirdPersonView ? -1 : 1) * playerViewX, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-0.025F, -0.025F, 0.025F);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		int i = fontRenderer.getStringWidth(nameTag) / 2;
		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos((double) (-i - 1), (double) (-1 + yOffset), 0.0D).color(0.0F, 0.0F, 0.0F, 1.00F).endVertex();
		vertexbuffer.pos((double) (-i - 1), (double) (8 + yOffset), 0.0D).color(0.0F, 0.0F, 0.0F, 1.00F).endVertex();
		vertexbuffer.pos((double) (i + 1), (double) (8 + yOffset), 0.0D).color(0.0F, 0.0F, 0.0F, 1.00F).endVertex();
		vertexbuffer.pos((double) (i + 1), (double) (-1 + yOffset), 0.0D).color(0.0F, 0.0F, 0.0F, 1.00F).endVertex();

		GL11.glScaled(scale, scale, scale);
		tessellator.draw();
		GlStateManager.enableTexture2D();

		fontRenderer.drawString(nameTag, -fontRenderer.getStringWidth(nameTag) / 2, yOffset, 0xFFFFFFFF);
		GlStateManager.enableDepth();

		GlStateManager.depthMask(false);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	@Override
	public void onDisable() {
		ModuleNameTags.isActive = false;
	}
}

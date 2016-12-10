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
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
		this.getModuleSettings().put("displayArmor", new ClientSettingBoolean("Armor", false));
		this.getModuleSettings().put("opacity", new ClientSettingBoolean("Opacity", true));
		this.getModuleValues().put("scale", new ValueBase("NameTag Scale", 3.0F, 1.0F, 10.0F, "Scale"));
	}

	public static void drawHealthTags(Entity entity, FontRenderer fontRenderer, String nameTag, float posX, float posY, float posZ, int yOffset, float playerViewY, float playerViewX, boolean thirdPersonView, boolean isSneaking) {
		final float distance = Wrapper.getPlayer().getDistanceToEntity(entity);
		final double scale = distance > 10.0F ? distance / instance.getModuleValues().get("scale").getValue() : 1.0F;
		final float alpha = instance.getValueOrDefault("opacity", Boolean.class, true) ? 0.25F : 1.00F;
		yOffset -= scale / 2;

		if (entity instanceof EntityOtherPlayerMP && instance.getValueOrDefault("displayHealth", Boolean.class, true)) {
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
		vertexbuffer.pos((double) (-i - 1), (double) (-1 + yOffset), 0.0D).color(0.0F, 0.0F, 0.0F, alpha).endVertex();
		vertexbuffer.pos((double) (-i - 1), (double) (8 + yOffset), 0.0D).color(0.0F, 0.0F, 0.0F, alpha).endVertex();
		vertexbuffer.pos((double) (i + 1), (double) (8 + yOffset), 0.0D).color(0.0F, 0.0F, 0.0F, alpha).endVertex();
		vertexbuffer.pos((double) (i + 1), (double) (-1 + yOffset), 0.0D).color(0.0F, 0.0F, 0.0F, alpha).endVertex();

		GL11.glScaled(scale, scale, scale);
		tessellator.draw();
		GlStateManager.enableTexture2D();

		fontRenderer.drawString(nameTag, -fontRenderer.getStringWidth(nameTag) / 2, yOffset, 0xFFFFFFFF);
		GlStateManager.enableDepth();

		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();

		if (entity instanceof EntityOtherPlayerMP && instance.getValueOrDefault("displayArmor", Boolean.class, false)) {
			ModuleNameTags.drawArmor((EntityOtherPlayerMP) entity, fontRenderer, posX, posY, posZ, playerViewY, playerViewX, thirdPersonView);
		}
	}

	private static void drawArmor(EntityOtherPlayerMP entityPlayer, FontRenderer fontRenderer, float posX, float posY, float posZ, float playerViewY, float playerViewX, boolean thirdPersonView) {
		RenderEntityItem renderEntity = (RenderEntityItem) Wrapper.getMinecraft().getRenderManager().getEntityRenderMap().get(EntityItem.class);

		GL11.glPushMatrix();
		GlStateManager.translate(posX, posY, posZ);
		GlStateManager.rotate(-playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

		ArrayList<ItemStack> itemList = new ArrayList<>();
		itemList.addAll(Arrays.asList(entityPlayer.inventory.armorInventory));
		itemList.add(entityPlayer.getHeldItemMainhand());

		int xIndex = 0;
		for (ItemStack itemStack : itemList) {
			int yIndex = 0;
			if (itemStack != null) {
				EntityItem entityItem = new EntityItem(Wrapper.getWorld(), posX, posY, posZ, itemStack);

				entityItem.hoverStart = 0;
				GlStateManager.scale(-0.01F, -0.01F, 0.01F);
				for (Map.Entry<Enchantment, Integer> enchantmentEntry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
					String displayName = enchantmentEntry.getKey().getTranslatedName(enchantmentEntry.getValue());
					displayName = displayName.replaceAll("enchantment.level.[0-9]+", "");
					displayName = displayName.substring(0, 4).toLowerCase() + ": " + ChatColor.GREEN + enchantmentEntry.getValue();
					fontRenderer.drawString(displayName, 75 - (xIndex * 50), -75 - (5 + (10 * yIndex)), 0xFFFFFFFF);

					yIndex++;
				}

				GlStateManager.scale(-100F, -100F, 100F);
				renderEntity.doRender(entityItem, -1D + (xIndex * 0.5D), 0, 0, Wrapper.getPlayer().rotationYaw, 0);
				RenderHelper.disableStandardItemLighting();
			}

			xIndex++;
		}
		GlStateManager.enableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	@Override
	public void onEnable() {
		ModuleNameTags.isActive = true;
	}

	@Override
	public void onDisable() {
		ModuleNameTags.isActive = false;
	}
}

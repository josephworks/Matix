package de.paxii.clarinet.gui.ingame.panel.theme.themes;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.theme.IClientTheme;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.render.ModuleXray;
import de.paxii.clarinet.util.chat.font.FontManager;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.render.GuiMethods;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

/**
 * Created by Lars on 26.06.2016.
 */
public class Matix2HDTheme implements IClientTheme {
	@Override
	public String getName() {
		return "Matix2HD";
	}

	@Override
	public void drawPanel(GuiPanel guiPanel, int mouseX, int mouseY) {
		int backgroundStart = 0xff585959;
		int backgroundEnd = 0x99585959;
		int enabledButton = 0xffA8A8A8;

		if (guiPanel.getPanelName().equalsIgnoreCase("player")) {
			backgroundStart = 0xff0F721F;
			backgroundEnd = 0x990F721F;
		} else if (guiPanel.getPanelName().equalsIgnoreCase("movement")) {
			backgroundStart = 0xff670B6E;
			backgroundEnd = 0x99670B6E;
		} else if (guiPanel.getPanelName().equalsIgnoreCase("combat")) {
			backgroundStart = 0xff70040A;
			backgroundEnd = 0x9970040A;
		} else if (guiPanel.getPanelName().equalsIgnoreCase("render")) {
			backgroundStart = 0xFFFF4D00;
			backgroundEnd = 0x99FF4D00;
		} else if (guiPanel.getPanelName().equalsIgnoreCase("world")) {
			backgroundStart = 0xff074770;
			backgroundEnd = 0x99074770;
		}

		GuiMethods.drawGradientRect(
				guiPanel.getPanelX(),
				guiPanel.getPanelY(),
				guiPanel.getPanelX() + guiPanel.getPanelWidth(),
				guiPanel.getPanelY() + (guiPanel.isOpened() ? guiPanel.getPanelHeight() : guiPanel.getTitleHeight()),
				backgroundStart,
				backgroundEnd
		);
		GuiMethods.drawRoundedRect(
				guiPanel.getPanelX() + guiPanel.getPanelWidth() - 11,
				guiPanel.getPanelY() + 2,
				guiPanel.getPanelX() + guiPanel.getPanelWidth() - 2,
				guiPanel.getPanelY() + guiPanel.getTitleHeight() - 2,
				enabledButton,
				enabledButton
		);
		FontManager.getDefaultFont().drawString(guiPanel.getPanelName(), guiPanel.getPanelX() + 4, guiPanel.getPanelY() - 2, 0xFFFFFFFF);
	}

	@Override
	public void drawButton(Module module, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered) {
		int buttonColor = 0x55000000;
		int textColor = 0xffffffff;

		if (module.isEnabled()) {
			buttonColor = 0xff609E3C;

			if (buttonHovered) {
				buttonColor = 0xffAE8C31;
			}
		} else {
			if (buttonHovered) {
				buttonColor = 0x33000000;
			}
		}

		GuiMethods.drawRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight - 1, buttonColor);
		FontManager.getDefaultFont().drawString(module.getName(), buttonX + 3, buttonY - 2, textColor);
	}

	@Override
	public void drawBlockButton(IBlockState iBlockState, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered) {
		int blockID = Block.getIdFromBlock(iBlockState.getBlock());
		boolean isEnabled = ModuleXray.getBlockList().contains(blockID);

		RenderHelper.enableGUIStandardItemLighting();
		Wrapper.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(iBlockState.getBlock(), 1), buttonX + 2, buttonY + 2);
		RenderHelper.disableStandardItemLighting();

		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, 255.0F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		Wrapper.getFontRenderer().drawSplitString(iBlockState.getBlock().getLocalizedName(), buttonX * 2 + 2, buttonY * 2 + 2, 38, 0xFFFFFFFF);
		GL11.glPopMatrix();

		GuiMethods.drawRoundedRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, isEnabled ? 0xFF00FF00 : 0xFFFF0000, 0);
	}

	@Override
	public void drawColorButton(String colorName, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered) {

	}

	@Override
	public void drawSlider(ValueBase valueBase, int sliderX, int sliderY, int sliderWidth, int sliderHeight, float dragX, boolean shouldRound) {
		DecimalFormat format = new DecimalFormat(shouldRound ? "0" : "0.0");

		GuiMethods.drawHLine(sliderX, sliderX + sliderWidth, sliderY + 12, 0xFFAAAAAA);
		GuiMethods.drawHLine(sliderX, sliderX + dragX, sliderY + 12, 0xFF0088FF);
		GuiMethods.drawFilledCircle(
				(dragX + 3 > sliderWidth ? (sliderX + sliderWidth) : (sliderX + (int) dragX + 3)),
				sliderY + 12,
				3,
				0xFF999999
		);

		FontManager.getDefaultFont().drawString(
				valueBase.getName() + ": " + format.format(valueBase.getValue()),
				sliderX,
				sliderY - 2,
				0xFFFFFFFF
		);

		IClientTheme.super.drawSlider(valueBase, sliderX, sliderY, sliderWidth, sliderHeight, dragX, shouldRound);
	}
}

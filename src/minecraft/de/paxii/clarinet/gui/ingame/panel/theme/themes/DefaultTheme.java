package de.paxii.clarinet.gui.ingame.panel.theme.themes;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.theme.GuiTheme;
import de.paxii.clarinet.gui.ingame.panel.theme.layout.DefaultThemeLayout;
import de.paxii.clarinet.gui.ingame.panel.theme.layout.GuiThemeLayout;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.render.ModuleXray;
import de.paxii.clarinet.util.chat.font.FontManager;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.render.GuiMethods;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class DefaultTheme implements GuiTheme {

  private static final GuiThemeLayout LAYOUT = new DefaultThemeLayout();

  private static final ResourceLocation PANEL_HEADER = DefaultTheme.getResource("panel-header");

  private static final ResourceLocation MODULE_ENABLED = DefaultTheme.getResource("module-enabled");
  private static final ResourceLocation MODULE_ENABLED_HOVERED = DefaultTheme.getResource("module-enabled-hovered");
  private static final ResourceLocation MODULE_DISABLED = DefaultTheme.getResource("module-disabled");
  private static final ResourceLocation MODULE_DISABLED_HOVERED = DefaultTheme.getResource("module-disabled-hovered");

  private static final ResourceLocation BUTTON_ENABLED = DefaultTheme.getResource("button-enabled");
  private static final ResourceLocation BUTTON_ENABLED_HOVERED = DefaultTheme.getResource("button-enabled-hovered");
  private static final ResourceLocation BUTTON_DISABLED = DefaultTheme.getResource("button-disabled");
  private static final ResourceLocation BUTTON_DISABLED_HOVERED = DefaultTheme.getResource("button-disabled-hovered");

  @Override
  public String getName() {
    return "Default";
  }

  @Override
  public void drawPanel(GuiPanel guiPanel, int mouseX, int mouseY) {
    // dynamically render panel header based on panel width
    for (int i = 0; i < (guiPanel.getPanelWidth() - 30) * 2; i += 12) {
      this.drawTexture(PANEL_HEADER, guiPanel.getPanelX() + 20 + (i / 2), guiPanel.getPanelY(), 0, 124, 12, 45);
    }
    this.drawTexture(PANEL_HEADER, guiPanel.getPanelX(), guiPanel.getPanelY(), 0, 0, 45, 45);
    this.drawTexture(PANEL_HEADER, guiPanel.getPanelX() + guiPanel.getPanelWidth() - 20, guiPanel.getPanelY(), 0, 64, 45, 45);

    FontManager.getBigUbuntuFont().drawCenteredString(guiPanel.getPanelName(), guiPanel.getPanelX() + (guiPanel.getPanelWidth() / 2), guiPanel.getPanelY() + (guiPanel.getTitleHeight() / 2) - ((int) FontManager.getBigUbuntuFont().getStringHeight(" ") / 2) + 1, 0xFFFFFFFF);

    if (guiPanel.isOpened()) {
      GuiMethods.drawGradientRect(
              guiPanel.getPanelX(),
              guiPanel.getPanelY() + 19,
              guiPanel.getPanelX() + guiPanel.getPanelWidth(),
              guiPanel.getPanelY() + (guiPanel.isOpened() ? guiPanel.getPanelHeight() : guiPanel.getTitleHeight()),
              0x99000000,
              0x99000000
      );
    }
  }

  @Override
  public void drawButton(String caption, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean active, boolean buttonHovered) {
    ResourceLocation resourceLocation;
    if (active) {
      if (buttonHovered) {
        resourceLocation = MODULE_ENABLED_HOVERED;
      } else {
        resourceLocation = MODULE_ENABLED;
      }
    } else {
      if (buttonHovered) {
        resourceLocation = MODULE_DISABLED_HOVERED;
      } else {
        resourceLocation = MODULE_DISABLED;
      }
    }

    this.drawTexture(resourceLocation, buttonX, buttonY, 0, 0, 256, 256);
    FontManager.getSmallUbuntuFont().drawCenteredString(caption, buttonX + (buttonWidth / 2), buttonY + (buttonHeight / 2) - 7, 0xFFFFFFFF);
  }

  @Override
  public void drawModuleButton(Module module, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered, boolean hasSettings, boolean displayHelp) {
    this.drawButton(module.getName(), buttonX, buttonY, buttonWidth, buttonHeight, module.isEnabled(), buttonHovered);

    if (hasSettings) {
      GuiMethods.drawRightTri(buttonX + buttonWidth + 2, buttonY + (buttonHeight / 2) + 1, 3, 0xFFFFFFFF);
    }

    if (displayHelp && module.getDescription().length() > 0) {
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.0F, 255.0F);
      int posX = Mouse.getX() / 2 + 10;
      int posY = (Display.getHeight() - Mouse.getY()) / 2;
      GuiMethods.drawRoundedRect(
              posX - 3,
              posY,
              posX + (int) FontManager.getUbuntuFont().getStringWidth(module.getDescription()),
              posY + Wrapper.getFontRenderer().FONT_HEIGHT + 3,
              0xff585959,
              0xff585959
      );
      FontManager.getUbuntuFont().drawString(module.getDescription(), posX, posY - 2, 0xFFFFFFFF);
      GL11.glPopMatrix();
    }
  }

  @Override
  public void drawCheckBox(String caption, boolean checked, int elementX, int elementY, int elementWidth, int elementHeight, boolean elementHovered) {
    this.drawButton(caption, elementX, elementY, elementWidth, elementHeight, checked, elementHovered);
  }

  @Override
  public void drawDropdown(String currentValue, String[] values, int elementX, int elementY, int elementWidth, int elementHeight, int defaultElementHeight, boolean opened, boolean elementHovered) {

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

    GuiMethods.drawHLine(sliderX, sliderX + sliderWidth, sliderY + 12, 0xFFD5D5D5);
    GuiMethods.drawHLine(sliderX, sliderX + dragX, sliderY + 12, 0xFF3D5EA9);
    GuiMethods.drawFilledCircle(
            (dragX + 3 > sliderWidth ? (sliderX + sliderWidth) : (sliderX + (int) dragX + 3)),
            sliderY + 12,
            3,
            0xFFFFFFFF
    );

    FontManager.getSmallUbuntuFont().drawString(
            valueBase.getDisplayName() + ": " + format.format(valueBase.getValue()),
            sliderX,
            sliderY - 4,
            0xFFFFFFFF
    );

    GuiTheme.super.drawSlider(valueBase, sliderX, sliderY, sliderWidth, sliderHeight, dragX, shouldRound);
  }

  @Override
  public GuiThemeLayout getLayout() {
    return LAYOUT;
  }

  protected void drawTexture(ResourceLocation resource, int x, int y, int textureX, int textureY, int width, int height) {
    GL11.glPushMatrix();
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glScaled(0.5, 0.5, 0.5);
    Wrapper.getMinecraft().getTextureManager().bindTexture(resource);
    GuiMethods.drawTexturedModalRect(x * 2, y * 2, textureX, textureY, width, height, 0.0D);
    GL11.glDisable(GL11.GL_BLEND);
    GL11.glPopMatrix();
  }

  protected static ResourceLocation getResource(String fileName) {
    return new ResourceLocation("matix", String.format("textures/matix-theme-default/%s.png", fileName));
  }
}

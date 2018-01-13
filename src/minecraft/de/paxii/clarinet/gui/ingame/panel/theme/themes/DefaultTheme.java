package de.paxii.clarinet.gui.ingame.panel.theme.themes;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.gui.ingame.panel.theme.IClientTheme;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.font.FontManager;
import de.paxii.clarinet.util.render.GuiMethods;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class DefaultTheme implements IClientTheme {

  private static final ResourceLocation PANEL_HEADER = DefaultTheme.getResource("panel-header");

  private static final ResourceLocation MODULE_ENABLED = DefaultTheme.getResource("module-enabled");
  private static final ResourceLocation MODULE_ENABLED_HOVERED = DefaultTheme.getResource("module-enabled-hovered");
  private static final ResourceLocation MODULE_DISABLED = DefaultTheme.getResource("module-disabled");
  private static final ResourceLocation MODULE_DISABLED_HOVERED = DefaultTheme.getResource("module-disabled-hovered");

  @Override
  public String getName() {
    return "Default";
  }

  @Override
  public void drawPanel(GuiPanel guiPanel, int mouseX, int mouseY) {
    this.drawTexture(PANEL_HEADER, guiPanel.getPanelX(), guiPanel.getPanelY(), 0, 0, 256, 256);
    FontManager.getUbuntuFont().drawCenteredString(guiPanel.getPanelName(), guiPanel.getPanelX() + (guiPanel.getPanelWidth() / 2), guiPanel.getPanelY() + (guiPanel.getTitleHeight() / 2) - ((int) FontManager.getUbuntuFont().getStringHeight(" ") / 2) + 1, 0xFFFFFFFF);

    if (guiPanel.isOpened()) {
      GuiMethods.drawGradientRect(
              guiPanel.getPanelX(),
              guiPanel.getPanelY() + 20,
              guiPanel.getPanelX() + guiPanel.getPanelWidth(),
              guiPanel.getPanelY() + (guiPanel.isOpened() ? guiPanel.getPanelHeight() : guiPanel.getTitleHeight()),
              0x99000000,
              0x99000000
      );
    }
  }

  @Override
  public void drawButton(String caption, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean active, boolean buttonHovered) {

  }

  @Override
  public void drawModuleButton(Module module, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered, boolean hasSettings, boolean displayHelp) {
    ResourceLocation resourceLocation;
    if (module.isEnabled()) {
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
    FontManager.getSmallUbuntuFont().drawCenteredString(module.getName(), buttonX + (buttonWidth / 2), buttonY + (buttonHeight / 2) - ((int) FontManager.getSmallUbuntuFont().getStringHeight(" ") / 2) + 1, 0xFFFFFFFF);
  }

  @Override
  public void drawCheckBox(String caption, boolean checked, int elementX, int elementY, int elementWidth, int elementHeight, boolean elementHovered) {

  }

  @Override
  public void drawDropdown(String currentValue, String[] values, int elementX, int elementY, int elementWidth, int elementHeight, int defaultElementHeight, boolean opened, boolean elementHovered) {

  }

  @Override
  public void drawBlockButton(IBlockState iBlockState, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered) {

  }

  @Override
  public void drawColorButton(String colorName, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered) {

  }

  @Override
  public int getElementWidth(PanelElement panelElement) {
    return 95;
  }

  @Override
  public int getElementHeight(PanelElement panelElement) {
    return 22;
  }

  protected void drawTexture(ResourceLocation resource, int x, int y, int textureX, int textureY, int width, int height) {
    GL11.glPushMatrix();
    GL11.glScaled(0.5, 0.5, 0.5);
    Wrapper.getMinecraft().getTextureManager().bindTexture(resource);
    GuiMethods.drawTexturedModalRect(x * 2, y * 2, textureX, textureY, width, height, 0.0D);
    GL11.glPopMatrix();
  }

  protected static ResourceLocation getResource(String fileName) {
    return new ResourceLocation("matix", String.format("textures/matix-theme-default/%s.png", fileName));
  }
}

package de.paxii.clarinet.gui.ingame.panel.theme.themes;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.theme.IClientTheme;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.render.ModuleXray;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.render.GuiMethods;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class DefaultClientTheme implements IClientTheme {
  @Getter
  private ArrayList<DefaultThemeColorObject> colorObjects;

  @Getter
  @Setter
  private DefaultThemeColorObject currentColor;

  public DefaultClientTheme() {
    this.colorObjects = new ArrayList<>();

    this.colorObjects.add(new DefaultThemeColorObject("Blue", 0xAA3F73FF, 0xAA272833, 0xCC8C9399, 0xCCD2CED1, 0xCC3F73FF, 0xCC759CFF, 0xFFFFFFFF));
    this.colorObjects.add(new DefaultThemeColorObject("Red", 0xAAFF0000, 0xAA272833, 0xCC8C9399, 0xCCD2CED1, 0xCCFF0000, 0xCCFF7070, 0xFFFFFFFF));
    this.colorObjects.add(new DefaultThemeColorObject("Yellow", 0xAAFFF200, 0xAA272833, 0xCC8C9399, 0xCCD2CED1, 0xCCFFF200, 0xCCFFFABA, 0xFFFFFFFF));
    this.colorObjects.add(new DefaultThemeColorObject("Green", 0xAA76FF00, 0xAA272833, 0xCC8C9399, 0xCCD2CED1, 0xCC76FF00, 0xCCBFFF87, 0xFFFFFFFF));
    this.colorObjects.add(new DefaultThemeColorObject("Purple", 0xAAA82599, 0xAA272833, 0xCC8C9399, 0xCCD2CED1, 0xCCA82599, 0xCCA0669A, 0xFFFFFFFF));
    this.colorObjects.add(new DefaultThemeColorObject("Ocker", 0xAAB4835A, 0xAA272833, 0xCC8C9399, 0xCCD2CED1, 0xCCB4835A, 0xCCB2967E, 0xFFFFFFFF));
    this.colorObjects.add(new DefaultThemeColorObject("Orange", 0xAAFF984F, 0xAA272833, 0xCC8C9399, 0xCCD2CED1, 0xCCFF984F, 0xCCFFB584, 0xFFFFFFFF));
    this.colorObjects.add(new DefaultThemeColorObject("Turquoise", 0xAA34F7EA, 0xAA272833, 0xCC8C9399, 0xCCD2CED1, 0xCC34F7EA, 0xCCBCF2EE, 0xFFFFFFFF));

    this.currentColor = this.colorObjects.get(0);
  }

  @Override
  public String getName() {
    return "Default";
  }

  @Override
  public void drawPanel(GuiPanel guiPanel, int mouseX, int mouseY) {
    if (guiPanel.isOpened()) {
      GuiMethods.drawRoundedRect(guiPanel.getPanelX(),
              guiPanel.getPanelY(),
              guiPanel.getPanelX() + guiPanel.getPanelWidth(),
              guiPanel.getPanelY() + guiPanel.getPanelHeight(),
              this.currentColor.getPanelBackground(), this.currentColor.getPanelBackground());
      GuiMethods.drawRoundedRect(guiPanel.getPanelX(),
              guiPanel.getPanelY(),
              guiPanel.getPanelX() + guiPanel.getPanelWidth(),
              guiPanel.getPanelY() + guiPanel.getTitleHeight(),
              this.currentColor.getPanelTitle(), this.currentColor.getPanelTitle());
      if (guiPanel.isCollapsible()) {
        GuiMethods.drawRoundedRect(
                guiPanel.getPanelX() + guiPanel.getPanelWidth() - 11,
                guiPanel.getPanelY() + 2,
                guiPanel.getPanelX() + guiPanel.getPanelWidth() - 2,
                guiPanel.getPanelY() + guiPanel.getTitleHeight() - 2,
                guiPanel.isMouseOverCollapseButton(mouseX, mouseY) ?
                        this.currentColor.getButtonDisabledBackgroundHovered() :
                        this.currentColor.getButtonDisabledBackground(),
                guiPanel.isMouseOverCollapseButton(mouseX, mouseY) ?
                        this.currentColor.getButtonDisabledBackgroundHovered() :
                        this.currentColor.getButtonDisabledBackground()
        );
      }
      Wrapper.getFontRenderer().drawString(guiPanel.getPanelName(),
              guiPanel.getPanelX() + 4, guiPanel.getPanelY() + 2,
              this.currentColor.getTextColor());
    } else {
      GuiMethods.drawRoundedRect(guiPanel.getPanelX(),
              guiPanel.getPanelY(),
              guiPanel.getPanelX() + guiPanel.getPanelWidth(),
              guiPanel.getPanelY() + guiPanel.getTitleHeight(),
              this.currentColor.getPanelTitle(), this.currentColor.getPanelTitle());
      if (guiPanel.isCollapsible()) {
        GuiMethods.drawRoundedRect(
                guiPanel.getPanelX() + guiPanel.getPanelWidth() - 11,
                guiPanel.getPanelY() + 2,
                guiPanel.getPanelX() + guiPanel.getPanelWidth() - 2,
                guiPanel.getPanelY() + guiPanel.getTitleHeight() - 2,
                guiPanel.isMouseOverCollapseButton(mouseX, mouseY) ? this.currentColor.getButtonDisabledBackgroundHovered() : this.currentColor.getButtonDisabledBackground(),
                guiPanel.isMouseOverCollapseButton(mouseX, mouseY) ? this.currentColor.getButtonDisabledBackgroundHovered() : this.currentColor.getButtonDisabledBackground()
        );
      }
      Wrapper.getFontRenderer().drawString(guiPanel.getPanelName(),
              guiPanel.getPanelX() + 4, guiPanel.getPanelY() + 2,
              this.currentColor.getTextColor());
    }
  }

  @Override
  public void drawButton(Module module, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered, boolean hasSettings, boolean displayHelp) {
    int buttonColor = module.isEnabled() ? (buttonHovered ? this.currentColor.getButtonEnabledBackgroundHovered()
            : this.currentColor.getButtonEnabledBackground())
            : (buttonHovered ? this.currentColor.getButtonDisabledBackgroundHovered()
            : this.currentColor.getButtonDisabledBackground());
    GuiMethods.drawRoundedRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight - 1, buttonColor, buttonColor);
    if (hasSettings) {
      GuiMethods.drawRightTri(buttonX + buttonWidth - 3, buttonY + (buttonHeight / 2), 3, 0xFFFFFFFF);
    }
    Wrapper.getFontRenderer().drawString(module.getName(), buttonX + 5, buttonY + 2, this.currentColor.getTextColor());

    if (displayHelp && module.getDescription().length() > 0) {
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.0F, 255.0F);
      int posX = Mouse.getX() / 2 + 10;
      int posY = (Display.getHeight() - Mouse.getY()) / 2;
      GuiMethods.drawRoundedRect(
              posX - 3,
              posY,
              posX + Wrapper.getFontRenderer().getStringWidth(module.getDescription()) + 3,
              posY + Wrapper.getFontRenderer().FONT_HEIGHT + 3,
              this.currentColor.getPanelBackground(),
              this.currentColor.getPanelBackground()
      );
      Wrapper.getFontRenderer().drawString(module.getDescription(), posX, posY + 2, 0xFFFFFFFF);
      GL11.glPopMatrix();
    }
  }

  @Override
  public void drawCheckBox(String caption, boolean checked, int elementX, int elementY, int elementWidth, int elementHeight, boolean elementHovered) {
    int buttonColor = checked ? (elementHovered ? this.currentColor.getButtonEnabledBackgroundHovered()
            : this.currentColor.getButtonEnabledBackground())
            : (elementHovered ? this.currentColor.getButtonDisabledBackgroundHovered()
            : this.currentColor.getButtonDisabledBackground());
    GuiMethods.drawRoundedRect(elementX, elementY, elementX + elementWidth,
            elementY + elementHeight - 1, buttonColor, buttonColor);
    Wrapper.getFontRenderer().drawString(caption, elementX + 5,
            elementY + 2, this.currentColor.getTextColor());
  }

  @Override
  public void drawDropdown(String currentValue, String[] values, int elementX, int elementY, int elementWidth, int elementHeight, int defaultElementHeight, boolean opened, boolean buttonHovered) {
    GuiMethods.drawBorderedRect(elementX, elementY, elementX + elementWidth, elementY + elementHeight, 1, 0x66000000, 0x33000000);
    if (opened) {
      GuiMethods.drawUpTri(elementX + elementWidth - 5, elementY + 5, 3, 0xFF000000);
    } else {
      GuiMethods.drawDownTri(elementX + elementWidth - 5, elementY + 8, 3, 0xFF000000);
    }
    Wrapper.getFontRenderer().drawString(currentValue, elementX + 5, elementY + 3, 0xFFFFFFFF);

    if (opened) {
      int indexY = elementY + defaultElementHeight;

      for (String value : values) {
        int lowerY = indexY + defaultElementHeight;

        GuiMethods.drawBorderedRect(elementX, indexY - 1, elementX + elementWidth, indexY + defaultElementHeight, 1, 0xFF000000, 0);
        Wrapper.getFontRenderer().drawString(value, elementX + 5, indexY + (defaultElementHeight / 2) - 4, currentValue.equals(value) ? 0xFF0000 : 0xFFFFFF);

        indexY = lowerY;
      }
    }
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
    int buttonColor = this.getCurrentColor().getColorName().equals(colorName) ? (buttonHovered ? this.currentColor.getButtonEnabledBackgroundHovered()
            : this.currentColor.getButtonEnabledBackground())
            : (buttonHovered ? this.currentColor.getButtonDisabledBackgroundHovered()
            : this.currentColor.getButtonDisabledBackground());
    GuiMethods.drawRoundedRect(buttonX, buttonY, buttonX + buttonWidth,
            buttonY + buttonHeight - 1, buttonColor, buttonColor);
    Wrapper.getFontRenderer().drawString(colorName, buttonX + 5,
            buttonY + 2, this.currentColor.getTextColor());
  }

  public DefaultThemeColorObject getColorObject(String colorName) {
    for (DefaultThemeColorObject colorObject : this.getColorObjects()) {
      if (colorObject.getColorName().equalsIgnoreCase(colorName))
        return colorObject;
    }

    return null;
  }

  @Override
  public void drawSlider(ValueBase valueBase, int sliderX, int sliderY,
                         int sliderWidth, int sliderHeight, float dragX, boolean shouldRound) {
    DecimalFormat format = new DecimalFormat(shouldRound ? "0" : "0.0");

    GuiMethods.drawHLine(sliderX, sliderX + sliderWidth, sliderY + 12,
            this.currentColor.getButtonDisabledBackground());
    GuiMethods.drawHLine(sliderX, sliderX + dragX, sliderY + 12,
            this.currentColor.getButtonEnabledBackground());
    GuiMethods.drawFilledCircle(
            (dragX + 3 > sliderWidth ? (sliderX + sliderWidth) : (sliderX
                    + (int) dragX + 3)), sliderY + 12, 3,
            this.currentColor.buttonDisabledBackgroundHovered);

    Wrapper.getFontRenderer().drawString(
            valueBase.getDisplayName() + ": "
                    + format.format(valueBase.getValue()), sliderX,
            sliderY, this.currentColor.getTextColor());

    IClientTheme.super.drawSlider(valueBase, sliderX, sliderY, sliderWidth,
            sliderHeight, dragX, shouldRound);
  }

  public class DefaultThemeColorObject {
    @Getter
    private String colorName;
    @Getter
    private int panelTitle = 0xAA3F73FF, panelBackground = 0xAA272833;
    @Getter
    private int buttonDisabledBackground = 0xCC8C9399,
            buttonDisabledBackgroundHovered = 0xCCD2CED1;
    @Getter
    private int buttonEnabledBackground = 0xCC3F73FF,
            buttonEnabledBackgroundHovered = 0xCC759CFF;
    @Getter
    private int textColor = 0xFFFFFFFF;

    public DefaultThemeColorObject(String colorName, int panelTitle, int panelBackground,
                                   int buttonDisabledBackground,
                                   int buttonDisabledBackgroundHovered,
                                   int buttonEnabledBackground,
                                   int buttonEnabledBackgroundHovered, int textColor) {
      this.colorName = colorName;
      this.panelTitle = panelTitle;
      this.panelBackground = panelBackground;
      this.buttonDisabledBackground = buttonDisabledBackground;
      this.buttonDisabledBackgroundHovered = buttonDisabledBackgroundHovered;
      this.buttonEnabledBackground = buttonEnabledBackground;
      this.buttonEnabledBackgroundHovered = buttonEnabledBackgroundHovered;
    }
  }
}

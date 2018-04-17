package de.paxii.clarinet.gui.ingame.panel;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.gui.ingame.panel.element.elements.PanelModuleButton;
import de.paxii.clarinet.gui.ingame.panel.theme.layout.ElementSpacing;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.DefaultTheme;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.EnumActionResult;

public class GuiPanel {
  @Getter
  private ArrayList<PanelElement> panelElements;
  @Getter
  @Setter
  private String panelName;
  @Getter
  @Setter
  private int panelX, panelY, panelWidth, panelHeight, titleHeight = 12, scrollY;
  private int dragX, dragY;
  @Getter
  @Setter
  private boolean dragging, draggable = true, opened, collapsible = true, pinned, visible = true, scrollable;

  public GuiPanel(String panelName, int panelX, int panelY) {
    this(panelName, panelX, panelY, 100, 200);
  }

  public GuiPanel(String panelName, int panelX, int panelY, int panelWidth, int panelHeight) {
    this(panelName, panelX, panelY, panelWidth, panelHeight, true);
  }

  public GuiPanel(String panelName, int panelX, int panelY, int panelWidth, int panelHeight, boolean isOpened) {
    this(panelName, panelX, panelY, panelWidth, panelHeight, isOpened, true);
  }

  public GuiPanel(String panelName, int panelX, int panelY, int panelWidth, int panelHeight, boolean isOpened, boolean isVisible) {
    this.panelName = panelName;
    this.panelX = panelX;
    this.panelY = panelY;
    this.panelWidth = panelWidth;
    this.panelHeight = panelHeight;
    this.panelElements = new ArrayList<>();

    this.setOpened(isOpened);
    this.setVisible(isVisible);

    this.addElements();
  }

  public void addElements() {
  }

  private int calculateHeight() {
    int height = 22;

    for (PanelElement panelElement : this.getPanelElements()) {
      height += panelElement.getHeight()
              + panelElement.getElementSpacing().getMarginTop()
              + panelElement.getElementSpacing().getMarginBottom();
    }

    return height;
  }

  public void drawPanel(int mouseX, int mouseY) {
    this.setPanelHeight(this.calculateHeight());

    if (this.isDragging()) {
      this.setPanelX(mouseX + dragX);
      this.setPanelY(mouseY + dragY);
    }

    Wrapper.getClickableGui().getCurrentTheme().drawPanel(this, mouseX, mouseY);

    if (this.isOpened()) {
      int index = 19;

      for (PanelElement panelElement : this.getPanelElements()) {
        ElementSpacing spacing = panelElement.getElementSpacing();
        index += spacing.getMarginTop();
        panelElement.drawElement(this.getPanelX() + spacing.getMarginLeft(), this.getPanelY() + index, mouseX, mouseY);
        index += panelElement.getHeight() + spacing.getMarginBottom();
      }
    }
  }

  public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
    if (this.isMouseOverCollapseButton(mouseX, mouseY) && buttonClicked == 0 && this.isCollapsible()) {
      this.setOpened(!this.isOpened());

      return;
    }

    if (this.isMouseOverTitle(mouseX, mouseY) && buttonClicked == 0 && this.isDraggable()) {
      this.setDragging(true);
      this.dragX = this.getPanelX() - mouseX;
      this.dragY = this.getPanelY() - mouseY;
    }

    if (this.isOpened() && this.isMouseOverPanel(mouseX, mouseY)) {
      for (PanelElement panelElement : this.getPanelElements()) {
        panelElement.mouseClicked(mouseX, mouseY, buttonClicked);
      }
    }
  }

  public EnumActionResult keyPressed(int keyCode) {
    EnumActionResult result = EnumActionResult.PASS;
    if (this.isOpened()) {
      for (PanelElement panelElement : this.getPanelElements()) {
        EnumActionResult actionResult = panelElement.keyPressed(keyCode);
        if (result == EnumActionResult.PASS) {
          result = actionResult;
        }
      }
    }

    return result;
  }

  public void setOpened(boolean opened) {
    this.opened = opened;

    this.getPanelElements().stream().filter(pE -> pE instanceof PanelModuleButton).forEach(pE -> {
      PanelModuleButton panelModuleButton = (PanelModuleButton) pE;

      if (panelModuleButton.getModuleSettings() != null) {
        panelModuleButton.getModuleSettings().setVisible(false);
      }
    });
  }

  public boolean isMouseOverPanel(int mouseX, int mouseY) {
    boolean rightX = mouseX >= this.getPanelX()
            && mouseX <= this.getPanelX() + this.getPanelWidth();
    boolean rightY = mouseY >= this.getPanelY()
            && mouseY <= this.getPanelY()
            + (this.isOpened() ? this.getPanelHeight() : this
            .getTitleHeight());

    return rightX && rightY;
  }

  public boolean isMouseOverTitle(int mouseX, int mouseY) {
    boolean rightX = mouseX >= this.getPanelX()
            && mouseX <= this.getPanelX() + this.getPanelWidth();
    boolean rightY = mouseY >= this.getPanelY()
            && mouseY <= this.getPanelY() + this.getTitleHeight();

    return rightX && rightY;
  }

  public boolean isMouseOverCollapseButton(int mouseX, int mouseY) {
    // FIXME: Position Collapse Button based on Layout
    if (Wrapper.getClickableGui().getCurrentTheme() instanceof DefaultTheme) {
      return mouseX >= this.getPanelX() + this.getPanelWidth() - 15 &&
              mouseX <= this.getPanelX() + this.getPanelWidth() - 5 &&
              mouseY >= this.getPanelY() + 2 &&
              mouseY <= this.getPanelY() + 20;
    }

    return mouseX >= this.getPanelX() + this.getPanelWidth() - 11 &&
            mouseX <= this.getPanelX() + this.getPanelWidth() - 2 &&
            mouseY >= this.getPanelY() + 2 &&
            mouseY <= this.getPanelY() + this.getTitleHeight() - 2;
  }

  public boolean isMouseOverAll(int mouseX, int mouseY) {
    if (isMouseOverPanel(mouseX, mouseY)) {
      for (GuiPanel guiPanel : Wrapper.getClickableGui().getGuiPanels()) {
        if (!guiPanel.isVisible())
          continue;

        if (guiPanel.isMouseOverPanel(mouseX, mouseY)
                && Wrapper.getClickableGui().getGuiPanels().indexOf(guiPanel) >
                Wrapper.getClickableGui().getGuiPanels().indexOf(this)) {
          return false;
        }
      }

      return true;
    }

    return false;
  }
}

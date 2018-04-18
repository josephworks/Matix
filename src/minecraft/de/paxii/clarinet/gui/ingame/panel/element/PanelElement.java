package de.paxii.clarinet.gui.ingame.panel.element;

import de.paxii.clarinet.gui.ingame.panel.theme.layout.ElementSpacing;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.EnumActionResult;

public class PanelElement {
  @Getter
  @Setter
  private int elementX, elementY;

  public PanelElement() {
  }

  public PanelElement(int elementX, int elementY) {
    this.elementX = elementX;
    this.elementY = elementY;
  }

  public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
    this.elementX = elementX;
    this.elementY = elementY;
  }

  public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
  }

  public void mouseMovedOrUp(int mouseX, int mouseY, int buttonClicked) {
  }

  public EnumActionResult keyPressed(int keyCode) {
    return EnumActionResult.PASS;
  }

  public boolean isMouseOverButton(int mouseX, int mouseY) {
    boolean rightX = mouseX > this.getElementX()
            && mouseX <= this.getElementX() + this.getWidth();
    boolean rightY = mouseY > this.getElementY()
            && mouseY <= this.getElementY() + this.getHeight();

    return rightX && rightY;
  }

  public int getWidth() {
    return this.getElementSpacing().getWidth();
  }

  public int getHeight() {
    return this.getElementSpacing().getHeight();
  }

  public ElementSpacing getElementSpacing() {
    return new ElementSpacing(0, 0);
  }
}

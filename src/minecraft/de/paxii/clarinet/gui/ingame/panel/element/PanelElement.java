package de.paxii.clarinet.gui.ingame.panel.element;

import de.paxii.clarinet.Wrapper;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

public class PanelElement {
  @Getter
  @Setter
  private int elementX, elementY, elementWidth, elementHeight, elementYOffset;

  public PanelElement() {
  }

  public PanelElement(int elementWidth, int elementHeight) {
    this.elementWidth = elementWidth;
    this.elementHeight = elementHeight;
  }

  public PanelElement(int elementX, int elementY, int elementWidth, int elementHeight) {
    this.elementX = elementX;
    this.elementY = elementY;
    this.elementWidth = elementWidth;
    this.elementHeight = elementHeight;
  }

  public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
    this.elementX = elementX;
    this.elementY = elementY;

    this.elementWidth = Wrapper.getClickableGui().getCurrentTheme().getElementWidth(this);
    this.elementHeight = Wrapper.getClickableGui().getCurrentTheme().getElementHeight(this);
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
            && mouseX <= this.getElementX() + this.getElementWidth();
    boolean rightY = mouseY > this.getElementY()
            && mouseY <= this.getElementY() + this.getElementHeight();

    return rightX && rightY;
  }
}

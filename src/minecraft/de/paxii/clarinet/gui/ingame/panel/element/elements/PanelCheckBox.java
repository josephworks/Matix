package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.AbstractPanelValueElement;

/**
 * Created by Lars on 31.07.2016.
 */
public class PanelCheckBox extends AbstractPanelValueElement<Boolean> {
  private String caption;

  public PanelCheckBox(String caption, boolean checked) {
    this.caption = caption;
    this.value = checked;
    this.setElementWidth(90);
    this.setElementHeight(12);
  }

  @Override
  public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
    boolean buttonHovered = this.isMouseOverButton(mouseX, mouseY);
    Wrapper.getClickableGui().getCurrentTheme().drawCheckBox(
            this.caption,
            this.value,
            elementX,
            elementY,
            this.getElementWidth(),
            this.getElementHeight(),
            buttonHovered
    );

    super.drawElement(elementX, elementY, mouseX, mouseY);
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
    if (this.isMouseOverButton(mouseX, mouseY)) {
      this.setValue(!this.getValue());
    }
  }
}

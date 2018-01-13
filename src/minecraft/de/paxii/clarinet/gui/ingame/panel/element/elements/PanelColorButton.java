package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.LegacyTheme;

public class PanelColorButton extends PanelElement {
  private final LegacyTheme.DefaultThemeColorObject colorObject;

  public PanelColorButton(LegacyTheme.DefaultThemeColorObject colorObject) {
    super(90, 12);

    this.colorObject = colorObject;
  }

  @Override
  public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
    boolean buttonHovered = this.isMouseOverButton(mouseX, mouseY);

    Wrapper.getClickableGui()
            .getCurrentTheme()
            .drawColorButton(this.colorObject.getColorName(), elementX, elementY, this.getElementWidth(),
                    this.getElementHeight(), buttonHovered);

    super.drawElement(elementX, elementY, mouseX, mouseY);
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
    if (this.isMouseOverButton(mouseX, mouseY)) {
      LegacyTheme clientTheme = (LegacyTheme) Wrapper.getClickableGui().getTheme("Legacy");

      if (clientTheme.getCurrentColor() != this.colorObject) {
        clientTheme.setCurrentColor(this.colorObject);
      }
    }
  }

  public boolean isMouseOverButton(int mouseX, int mouseY) {
    boolean rightX = mouseX > this.getElementX()
            && mouseX <= this.getElementX() + this.getElementWidth();
    boolean rightY = mouseY > this.getElementY()
            && mouseY <= this.getElementY() + this.getElementHeight();

    return rightX && rightY;
  }
}

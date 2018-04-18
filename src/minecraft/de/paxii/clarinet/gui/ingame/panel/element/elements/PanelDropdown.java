package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.AbstractPanelValueElement;
import de.paxii.clarinet.gui.ingame.panel.theme.layout.ElementSpacing;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lars on 27.08.2016.
 */
public class PanelDropdown extends AbstractPanelValueElement<String> {

  private int defaultElementHeight = 12;

  @Getter
  @Setter
  private String[] values;
  private boolean opened;

  public PanelDropdown(String value, Collection<String> values) {
    this(value, values.toArray(new String[values.size()]));
  }

  public PanelDropdown(String value, String[] values) {
    this.value = value;
    this.values = values;

    this.defaultElementHeight = this.getElementSpacing().getHeight();
  }

  @Override
  public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
    Wrapper.getClickableGui().getCurrentTheme().drawDropdown(
            this.value,
            this.values,
            this.getElementX(),
            this.getElementY(),
            this.getWidth(),
            this.getHeight(),
            this.defaultElementHeight,
            this.opened,
            this.isMouseOverButton(mouseX, mouseY)
    );

    super.drawElement(elementX, elementY, mouseX, mouseY);
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
    if (this.isMouseOverButton(mouseX, mouseY)) {
      this.setOpened(!this.opened);
    } else {
      if (this.opened) {
        int hovered = this.getHoveredId(mouseX, mouseY);

        if (hovered != -1) {
          this.setValue(this.values[hovered]);
          this.setOpened(false);
        }
      }
    }

    super.mouseClicked(mouseX, mouseY, buttonClicked);
  }

  private void setOpened(boolean opened) {
    this.opened = opened;
    this.getElementSpacing().setHeight(opened ? this.calculateHeight() : this.defaultElementHeight);
  }

  @Override
  public boolean isMouseOverButton(int mouseX, int mouseY) {
    boolean rightX = mouseX > this.getElementX()
            && mouseX <= this.getElementX() + this.getWidth();
    boolean rightY = mouseY > this.getElementY()
            && mouseY <= this.getElementY() + this.getHeight();

    return rightX && rightY;
  }

  private int getHoveredId(int mouseX, int mouseY) {
    int indexY = this.getElementY() + this.getHeight();

    for (int i = 0; i < this.values.length; i++) {
      int lowerY = indexY + this.getHeight();

      boolean hoverX = mouseX >= this.getElementX() && mouseX <= this.getElementX() + this.getWidth();
      boolean hoverY = mouseY >= indexY && mouseY <= lowerY;

      if (hoverX && hoverY) {
        return i;
      }

      indexY = lowerY;
    }

    return -1;
  }

  private int calculateHeight() {
    return this.defaultElementHeight + this.values.length * this.defaultElementHeight;
  }

  @Override
  public ElementSpacing getElementSpacing() {
    return Wrapper.getClickableGui().getCurrentTheme().getLayout().getDropdownLayout();
  }
}

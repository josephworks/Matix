package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.gui.ingame.panel.theme.layout.ElementSpacing;
import de.paxii.clarinet.util.function.TwoArgumentsFunction;

import lombok.Setter;
import net.minecraft.util.EnumActionResult;

/**
 * Created by Lars on 05.05.17.
 */
public class PanelKeyBindButton extends PanelElement {

  @Setter
  private String caption;
  private TwoArgumentsFunction<Boolean, Integer, PanelKeyBindButton> callback;
  private boolean listening;

  public PanelKeyBindButton(
      String caption,
      TwoArgumentsFunction<Boolean, Integer, PanelKeyBindButton> callback) {
    super(90, 12);

    this.caption = caption;
    this.callback = callback;
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
    if (this.listening) {
      this.keyPressed(buttonClicked - 100);
    } else if (this.isMouseOverButton(mouseX, mouseY)) {
      this.listening = true;
    }
    super.mouseClicked(mouseX, mouseY, buttonClicked);
  }

  @Override
  public EnumActionResult keyPressed(int keyCode) {
    if (this.listening) {
      if (!this.callback.apply(keyCode, this)) {
        this.listening = false;
      }

      return EnumActionResult.SUCCESS;
    }

    return EnumActionResult.PASS;
  }

  @Override
  public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
    Wrapper.getClickableGui()
        .getCurrentTheme()
        .drawButton(this.caption, elementX, elementY, this.getWidth(),
            this.getHeight(), this.listening, this.isMouseOverButton(mouseX, mouseY));

    super.drawElement(elementX, elementY, mouseX, mouseY);
  }

  @Override
  public ElementSpacing getElementSpacing() {
    return Wrapper.getClickableGui().getCurrentTheme().getLayout().getButtonLayout();
  }
}

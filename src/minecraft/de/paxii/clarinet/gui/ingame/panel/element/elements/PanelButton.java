package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.module.Module;

public class PanelButton extends PanelElement {
	private Module module;

	public PanelButton(Module module) {
		super(90, 12);

		this.module = module;
	}

	@Override
	public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
		boolean buttonHovered = this.isMouseOverButton(mouseX, mouseY);

		Wrapper.getClickableGui()
				.getCurrentTheme()
				.drawButton(module, elementX, elementY, this.getElementWidth(),
						this.getElementHeight(), buttonHovered);

		super.drawElement(elementX, elementY, mouseX, mouseY);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
		if (this.isMouseOverButton(mouseX, mouseY)) {
			this.module.toggle();
		}
	}
}

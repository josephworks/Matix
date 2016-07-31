package de.paxii.clarinet.gui.ingame.panel.element;

import lombok.Getter;
import lombok.Setter;

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
	}

	public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
	}

	public void mouseMovedOrUp(int mouseX, int mouseY, int buttonClicked) {
	}

	public boolean isMouseOverButton(int mouseX, int mouseY) {
		boolean rightX = mouseX > this.getElementX()
				&& mouseX <= this.getElementX() + this.getElementWidth();
		boolean rightY = mouseY > this.getElementY()
				&& mouseY <= this.getElementY() + this.getElementHeight();

		return rightX && rightY;
	}
}

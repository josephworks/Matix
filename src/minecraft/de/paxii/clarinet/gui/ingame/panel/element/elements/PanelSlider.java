package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.util.module.settings.ValueBase;
import lombok.Getter;
import lombok.Setter;

public class PanelSlider extends PanelElement {
	private float dragX, lastDragX;

	@Getter
	private ValueBase sliderValue;
	@Getter
	@Setter
	private boolean shouldRound;
	@Getter
	@Setter
	private boolean isDragging;

	public PanelSlider(ValueBase sliderValue, boolean shouldRound) {
		super(110, 15);

		this.sliderValue = sliderValue;
		this.shouldRound = shouldRound;
		setValue(sliderValue.getValue());
	}

	private void dragSlider(int mouseX) {
		dragX = mouseX - lastDragX;
	}

	@Override
	public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
		this.setElementX(elementX);
		this.setElementY(elementY);

		this.setValue(this.sliderValue.getValue());

		if (this.isDragging()) {
			this.dragSlider(mouseX);
		}

		if (this.dragX < 0) {
			this.dragX = 0;
		}

		if (this.dragX > this.getElementWidth()) {
			this.dragX = this.getElementWidth();
		}

		Wrapper.getClickableGui()
				.getCurrentTheme()
				.drawSlider(sliderValue, this.getElementX(),
						this.getElementY(), this.getElementWidth(),
						this.getElementHeight(), dragX, shouldRound);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
		if (buttonClicked == 0) {
			if (this.isMouseOverSlider(mouseX, mouseY)) {
				this.lastDragX = mouseX - this.dragX;
				this.setDragging(true);
			}
		}
	}

	@Override
	public void mouseMovedOrUp(int mouseX, int mouseY, int buttonClicked) {
		if (buttonClicked == 0) {
			this.setDragging(false);
		}
	}

	public boolean isMouseOverSlider(int mouseX, int mouseY) {
		boolean rightX = mouseX >= this.getElementX() + dragX - 3
				&& mouseX <= this.getElementX() + dragX + 6 + 3;
		boolean rightY = mouseY >= this.getElementY()
				&& mouseY <= this.getElementY() + this.getElementHeight();

		return rightX && rightY;
	}

	public void setValue(float value) {
		sliderValue.setValue(value);

		value -= this.sliderValue.getMin();

		float fraction = this.getElementWidth()
				/ (this.sliderValue.getMax() - this.sliderValue.getMin());
		dragX = fraction * value;
	}
}

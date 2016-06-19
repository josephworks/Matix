package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.util.objects.IntObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Lars on 27.05.2016.
 */
public class PanelBlockRow extends PanelElement {
	@Getter
	private ArrayList<PanelBlockButton> blockButtons;

	public PanelBlockRow(PanelBlockButton... blockButtons) {
		super(190, 20);

		IntObject width = new IntObject(10);
		Arrays.stream(blockButtons).forEach((blockButton -> width.add(blockButton.getElementWidth())));
		this.setElementWidth(width.getInteger());

		this.blockButtons = new ArrayList<>();
		this.blockButtons.addAll(Arrays.asList(blockButtons));
	}

	@Override
	public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
		IntObject indexX = new IntObject(elementX);

		this.blockButtons.forEach((blockButton -> {
			blockButton.drawElement(indexX.getInteger(), elementY, mouseX, mouseY);

			indexX.add(blockButton.getElementWidth());
		}));

		super.drawElement(elementX, elementY, mouseX, mouseY);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
		this.blockButtons.forEach((blockButton -> blockButton.mouseClicked(mouseX, mouseY, buttonClicked)));

		super.mouseClicked(mouseX, mouseY, buttonClicked);
	}
}

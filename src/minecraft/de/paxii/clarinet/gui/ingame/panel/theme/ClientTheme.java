package de.paxii.clarinet.gui.ingame.panel.theme;

import de.paxii.clarinet.util.module.settings.ValueBase;

public abstract class ClientTheme implements IClientTheme {
	@Override
	public void drawSlider(ValueBase valueBase, int sliderX, int sliderY, int sliderWidth, int sliderHeight, float dragX, boolean shouldRound) {
		float max = valueBase.getMax();
		float min = valueBase.getMin();
		float fraction = sliderWidth / (max - min);
		valueBase.setValue(shouldRound ? (int) (dragX / fraction) + min : (dragX / fraction) + min);
	}
}

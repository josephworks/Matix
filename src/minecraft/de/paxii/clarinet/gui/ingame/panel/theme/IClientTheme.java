package de.paxii.clarinet.gui.ingame.panel.theme;

import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.module.settings.ValueBase;
import net.minecraft.block.state.IBlockState;

public interface IClientTheme {
	String getName();

	void drawPanel(GuiPanel guiPanel, int mouseX, int mouseY);

	void drawButton(Module module, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered);

	void drawBlockButton(IBlockState iBlockState, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered);

	void drawColorButton(String colorName, int buttonX, int buttonY, int buttonWidth, int buttonHeight, boolean buttonHovered);

	default void drawSlider(ValueBase valueBase, int sliderX, int sliderY, int sliderWidth, int sliderHeight, float dragX, boolean shouldRound) {
		float max = valueBase.getMax();
		float min = valueBase.getMin();
		float fraction = sliderWidth / (max - min);
		valueBase.setValue(shouldRound ? (int) (dragX / fraction) + min : (dragX / fraction) + min);
	}
}

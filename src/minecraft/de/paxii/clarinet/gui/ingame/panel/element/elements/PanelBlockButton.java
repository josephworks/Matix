package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.module.render.ModuleXray;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * Created by Lars on 06.05.2016.
 */
public class PanelBlockButton extends PanelElement {
	@Getter
	private IBlockState iBlockState;

	public PanelBlockButton(IBlockState iBlockState) {
		super(20, 20);

		this.iBlockState = iBlockState;
	}

	@Override
	public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
		boolean buttonHovered = this.isMouseOverButton(mouseX, mouseY);

		Wrapper.getClickableGui()
		       .getCurrentTheme()
		       .drawBlockButton(this.iBlockState, elementX, elementY, this.getElementWidth(),
				       this.getElementHeight(), buttonHovered);

		super.drawElement(elementX, elementY, mouseX, mouseY);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
		if (this.isMouseOverButton(mouseX, mouseY)) {
			int blockID = Block.getIdFromBlock(this.iBlockState.getBlock());

			if (ModuleXray.getBlockList().contains(blockID)) {
				ModuleXray moduleXray = (ModuleXray) Wrapper.getModuleManager().getModule("Xray");
				moduleXray.removeBlock(blockID);
			} else {
				ModuleXray.getBlockList().add(blockID);
			}

			Wrapper.getMinecraft().renderGlobal.loadRenderers();
		}
	}
}

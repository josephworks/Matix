package de.paxii.clarinet.gui.ingame.settings;

import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import lombok.Getter;

public class PanelSettingsObject {
	@Getter
	private int posX, posY;
	@Getter
	private boolean opened, pinned, visible;

	public PanelSettingsObject(GuiPanel guiPanel) {
		this.posX = guiPanel.getPanelX();
		this.posY = guiPanel.getPanelY();
		this.opened = guiPanel.isOpened();
		this.pinned = guiPanel.isPinned();
		this.visible = guiPanel.isVisible();
	}
}

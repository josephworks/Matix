package de.paxii.clarinet.gui.ingame.panel;

import de.paxii.clarinet.module.Module;

/**
 * Created by Lars on 31.07.2016.
 */
public class GuiPanelModuleSettings extends GuiPanel {
	public GuiPanelModuleSettings(Module module, int panelX, int panelY) {
		super(module.getName() + " Settings", panelX, panelY);
		this.setVisible(false);
		this.setDraggable(false);
		this.setCollapsible(false);
	}
}

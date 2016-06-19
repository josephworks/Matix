package de.paxii.clarinet.gui.ingame.settings;

import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lars on 14.02.2016.
 */
public class PanelSettingsContainer {
	@Getter
	@Setter
	private HashMap<String, PanelSettingsObject> panelSettings;

	public PanelSettingsContainer(ArrayList<GuiPanel> panels) {
		this.panelSettings = new HashMap<>();

		panels.forEach((panel) -> panelSettings.put(panel.getPanelName(), new PanelSettingsObject(panel)));
	}

	public PanelSettingsContainer(HashMap<String, PanelSettingsObject> panelSettings) {
		this.panelSettings = panelSettings;
	}
}

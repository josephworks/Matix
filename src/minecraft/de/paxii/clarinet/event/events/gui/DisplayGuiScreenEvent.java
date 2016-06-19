package de.paxii.clarinet.event.events.gui;

import de.paxii.clarinet.event.events.type.EventCancellable;
import net.minecraft.client.gui.GuiScreen;

public class DisplayGuiScreenEvent extends EventCancellable {
	private GuiScreen guiScreen;

	public DisplayGuiScreenEvent(GuiScreen guiScreen) {
		this.guiScreen = guiScreen;
	}

	public void setGuiScreen(GuiScreen guiScreen) {
		this.guiScreen = guiScreen;
	}

	public GuiScreen getGuiScreen() {
		return this.guiScreen;
	}
}

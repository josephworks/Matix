package de.paxii.clarinet.gui.menu.login;

import de.paxii.clarinet.Wrapper;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltList extends GuiListExtended {
	private GuiListExtended.IGuiListEntry[] altEntries;
	private GuiListExtended.IGuiListEntry selectedAlt;

	public GuiAltList(GuiScreen parentScreen) {
		super(Wrapper.getMinecraft(), Wrapper.getScaledResolution()
				.getScaledWidth(), Wrapper.getScaledResolution()
				.getScaledHeight(), 63, Wrapper.getScaledResolution()
				.getScaledHeight() - 55, 20);

		if (parentScreen instanceof GuiAltManager) {
			GuiAltManager altManager = (GuiAltManager) parentScreen;

			this.altEntries = new GuiListExtended.IGuiListEntry[altManager
					.getAltList().size()];

			int index = 0;
			for (AltObject alt : altManager.getAltList()) {
				this.altEntries[index] = new GuiAltListEntry(alt, parentScreen);

				index++;
			}
		} else {
			this.altEntries = new GuiListExtended.IGuiListEntry[0];
		}

	}

	@Override
	public IGuiListEntry getListEntry(int var1) {
		return this.altEntries[var1];
	}

	@Override
	protected int getSize() {
		return this.altEntries.length;
	}
}

package de.paxii.clarinet.gui.menu.controls;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

import java.util.Map.Entry;
import java.util.TreeMap;

public class GuiKeybindsList extends GuiListExtended {
	private final GuiScreen parentScreen;
	private final GuiListExtended.IGuiListEntry[] keyEntries;

	public GuiKeybindsList(GuiScreen parentScreen) {
		super(
				Wrapper.getMinecraft(),
				Wrapper.getScaledResolution().getScaledWidth(),
				Wrapper.getScaledResolution().getScaledHeight(),
				63,
				Wrapper.getScaledResolution().getScaledHeight() - 32,
				20
		);
		this.parentScreen = parentScreen;
		this.keyEntries = new GuiListExtended.IGuiListEntry[Wrapper.getModuleManager().getModuleList().size()];

		int index = 0;

		TreeMap<String, Module> sortedModules = new TreeMap<>(Wrapper.getModuleManager().getModuleList());
		for (Entry<String, Module> moduleEntry : sortedModules.entrySet()) {
			this.keyEntries[index] = new GuiListEntryModuleKey(moduleEntry.getValue(), parentScreen);
			index++;
		}
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return this.keyEntries[index];
	}

	@Override
	protected int getSize() {
		return this.keyEntries.length;
	}
}
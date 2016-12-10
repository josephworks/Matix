package de.paxii.clarinet.gui.menu.store.module;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.module.store.ModuleEntry;
import de.paxii.clarinet.util.module.store.ModuleStore;
import de.paxii.clarinet.util.render.GuiMethods;
import lombok.Getter;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

/**
 * Created by Lars on 07.02.2016.
 */
public class GuiListEntryExternalModule implements GuiListExtended.IGuiListEntry {
	private final GuiScreen parentScreen;
	@Getter
	private ModuleEntry moduleEntry;
	private int x, y, width, height;

	public GuiListEntryExternalModule(ModuleEntry moduleEntry, GuiScreen parentScreen) {
		this.moduleEntry = moduleEntry;
		this.parentScreen = parentScreen;
	}

	@Override
	public void setSelected(int var1, int var2, int var3) {

	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
		GuiModuleStore guiModuleStore = (GuiModuleStore) this.parentScreen;

		if (isSelected) {
			guiModuleStore.setSelectedButton(this);
		}

		this.x = x;
		this.y = y;
		this.width = listWidth;
		this.height = slotHeight;

		if (guiModuleStore.getPressedButton() != null && guiModuleStore.getPressedButton().getModuleEntry().getModule().equals(this.getModuleEntry().getModule())) {
			GuiMethods.drawBorderedRect(this.x, this.y, this.x + this.width, this.y + this.height, 2.0F, 0xffFF8800, 0x00000000);
		} else {
			int color = 0xffffffff;

			if (ModuleStore.isModuleInstalled(this.getModuleEntry().getModule())) {
				color = 0xff00ff00;
			}

			if (ModuleStore.isModuleInstalled(this.getModuleEntry().getModule()) && !ModuleStore.isModuleUptoDate(this.getModuleEntry().getModule())) {
				color = 0xffff0000;
			}

			GuiMethods.drawBorderedRect(this.x, this.y, this.x + this.width, this.y + this.height, 1.0F, color, 0x00000000);
		}

		String displayName = this.getModuleEntry().getModule() + (this.getModuleEntry().getVersion() != null ? " - V" + this.getModuleEntry().getVersion() : "");
		Wrapper.getFontRenderer().drawString(displayName, x + 5, y + 5, 0xffffff);
		Wrapper.getFontRenderer().drawSplitString(this.getModuleEntry().getDescription(), x + 5, y + 15, this.width - 5, 0x888888);
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
		if ((mouseX >= this.x && mouseX <= this.x + this.width)
				&& (mouseY >= this.y && mouseY <= this.y + this.height)) {
			GuiModuleStore guiModuleStore = (GuiModuleStore) this.parentScreen;
			guiModuleStore.setPressedButton(this);

			return true;
		}

		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int var3, int var4, int var5, int var6) {
	}
}

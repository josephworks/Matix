package de.paxii.clarinet.gui.menu.controls;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import lombok.Getter;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GuiListEntryModuleKey implements GuiListExtended.IGuiListEntry {
	private final GuiScreen parentScreen;
	@Getter
	private Module module;
	private int x, y, width, height;

	public GuiListEntryModuleKey(Module module, GuiScreen parentScreen) {
		this.module = module;
		this.parentScreen = parentScreen;
	}

	@Override
	public void setSelected(int var1, int var2, int var3) {
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
		GuiKeybinds guiKeybinds = (GuiKeybinds) this.parentScreen;

		if (isSelected) {
			guiKeybinds.setSelectedButton(this);
		}

		this.x = x;
		this.y = y;
		this.width = listWidth;
		this.height = slotHeight;

		Wrapper.getFontRenderer().drawString(this.getModule().getName(), x + 5, y + 5, 0xffffff);
		String keyString = "NONE";

		if (this.getModule().getKey() != -1) {
			if (this.getModule().getKey() >= 0) {
				keyString = Keyboard.getKeyName(this.getModule().getKey());
			} else {
				keyString = "Mouse " + (this.getModule().getKey() + 101);
			}
		}

		if (guiKeybinds.isShouldListen() && guiKeybinds.getPressedButton() != null && guiKeybinds.getPressedButton().getModule().getName().equals(this.getModule().getName())) {
			keyString = "Press a key";
			Wrapper.getFontRenderer().drawString(keyString, x + listWidth - Wrapper.getFontRenderer().getStringWidth(keyString), y + 5, 0xF7E700);
		} else {
			Wrapper.getFontRenderer().drawString(keyString, x + listWidth - Wrapper.getFontRenderer().getStringWidth(keyString), y + 5, 0xffffff);
		}
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
		if ((mouseX >= this.x && mouseX <= this.x + this.width)
				&& (mouseY >= this.y && mouseY <= this.y + this.height)) {
			GuiKeybinds guiKeybinds = (GuiKeybinds) this.parentScreen;
			guiKeybinds.setPressedButton(this);
			guiKeybinds.setShouldListen(true);

			return true;
		}

		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int var3, int var4, int var5, int var6) {
	}
}
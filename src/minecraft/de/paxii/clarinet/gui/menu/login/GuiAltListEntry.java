package de.paxii.clarinet.gui.menu.login;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.render.GuiMethods;
import lombok.Getter;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltListEntry implements GuiListExtended.IGuiListEntry {
	private GuiScreen parentScreen;
	@Getter
	private AltObject alt;

	private int x, y, width, height;

	public GuiAltListEntry(AltObject alt, GuiScreen parentScreen) {
		this.alt = alt;
		this.parentScreen = parentScreen;
	}

	@Override
	public void setSelected(int var1, int var2, int var3) {
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth,
	                      int slotHeight, int mouseX, int mouseY, boolean isSelected) {
		GuiAltManager altManager = (GuiAltManager) parentScreen;

		this.x = x;
		this.y = y;
		this.width = listWidth;
		this.height = slotHeight;

		if (altManager.getPressedSlot() == this) {
			GuiMethods.drawBorderedRect(x, y, x + listWidth, y + slotHeight,
					1.0F, 0xFFFFFFFF, 0x000000);
		}

		Wrapper.getFontRenderer().drawString(this.alt.getUserName(), x + 5,
				y + 5, 0xffffff);
		if (this.alt.getEmail().length() > 0) {
			Wrapper.getFontRenderer().drawString(
					", " + this.alt.getEmail(),
					x
							+ Wrapper.getFontRenderer().getStringWidth(
							this.alt.getUserName()) + 5, y + 5,
					0xffffff);
		}
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY,
	                            int p_148278_4_, int p_148278_5_, int p_148278_6_) {
		if ((mouseX >= this.x && mouseX <= this.x + this.width)
				&& (mouseY >= this.y && mouseY <= this.y + this.height)) {
			GuiAltManager altManager = (GuiAltManager) parentScreen;

			altManager.setPressedSlot(this);

			return true;
		}

		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int var3, int var4, int var5, int var6) {
	}

}

package de.paxii.clarinet.gui.menu.controls;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.Wrapper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiKeybinds extends GuiScreen {
	private GuiScreen parentScreen;

	private GuiKeybindsList keybindsList;

	@Getter
	@Setter
	private GuiListEntryModuleKey selectedButton;
	@Getter
	@Setter
	private GuiListEntryModuleKey pressedButton;
	@Getter
	private int pressedKey;
	@Getter
	@Setter
	boolean shouldListen;

	private GuiButton doneButton;

	public GuiKeybinds(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;

		this.keybindsList = new GuiKeybindsList(this);
	}

	@Override
	public void initGui() {
		this.doneButton = new GuiButton(0, this.width / 2 - 50, this.height - 25, 100, 20, "Done");
		this.buttonList.add(this.doneButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			if (this.isShouldListen()) {
				this.getPressedButton().getModule().setKey(-1);

				this.setShouldListen(false);
				this.setPressedButton(null);
			} else {
				Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
			}
		}

		if (this.isShouldListen()) {
			if (this.getPressedButton() != null) {
				this.getPressedButton().getModule().setKey(keyCode);
			}

			this.setShouldListen(false);
			this.pressedKey = keyCode;
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		this.keybindsList.handleMouseInput();
		super.handleMouseInput();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == this.doneButton.id) {
			Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.keyTyped(' ', mouseButton - 100);
		this.keybindsList.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		this.keybindsList.mouseReleased(mouseX, mouseY, state);
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		if (this.keybindsList != null) {
			this.keybindsList.drawScreen(mouseX, mouseY, partialTicks);
		}

		this.drawCenteredString(this.fontRendererObj, Client.getClientName() + " Keys", this.width / 2, 8, 16777215);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
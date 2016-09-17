package de.paxii.clarinet.gui.menu.login;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.login.mcleaks.MCLeaksLoginBridge;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GuiMcLeaksLogin extends GuiScreen {
	private final GuiScreen parentScreen;

	private GuiTextField userNameField;

	private String errorMessage;
	private boolean displayError;
	private final TimeManager timeManager;

	public GuiMcLeaksLogin(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;

		this.timeManager = new TimeManager();
	}

	@Override
	public void initGui() {
		int startX = this.width / 2 - 100;
		int width = 200;

		this.userNameField = new GuiTextField(3, Wrapper.getFontRenderer(), startX, this.height / 2 - 50, width, 20);

		this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 25, 100, 20, "Cancel"));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 55, this.height - 25, 100, 20, "Redeem"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height - 25, 100, 20, "Auto"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		this.userNameField.drawTextBox();

		if (this.displayError) {
			this.drawCenteredString(Wrapper.getFontRenderer(), this.errorMessage, this.width / 2, 30, 0xff0000);

			if (this.timeManager.sleep(5000L)) {
				this.displayError = false;
			}
		} else {
			this.timeManager.updateLast();
		}

		this.drawCenteredString(Wrapper.getFontRenderer(), "Redeem McLeaks Token", this.width / 2, 5, 0xffffff);
		this.drawCenteredString(Wrapper.getFontRenderer(), "Token:", this.width / 2, this.height / 2 - 65, 0xffffff);

		this.timeManager.updateTimer();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 15) {
			this.userNameField.setFocused(true);
		}

		this.userNameField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
		} else if (button.id == 1) {
			String userName = this.userNameField.getText();

			if (userName.length() == 0) {
				this.errorMessage = "Invalid Token!";
				this.displayError = true;
			} else {
				this.displayError = false;

				if (MCLeaksLoginBridge.loginWithToken(userName) != null) {
					Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
				} else {
					this.errorMessage = "There was an error when redeeming the token.";
					this.displayError = true;
				}
			}
		} else if (button.id == 2) {
			this.displayError = false;

			if (MCLeaksLoginBridge.loginWithNewToken() != null) {
				Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
			} else {
				this.errorMessage = "There was an error when redeeming the token.";
				this.displayError = true;
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
			throws IOException {
		this.userNameField.mouseClicked(mouseX, mouseY, mouseButton);

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}

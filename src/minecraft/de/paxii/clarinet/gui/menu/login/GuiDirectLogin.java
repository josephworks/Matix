package de.paxii.clarinet.gui.menu.login;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.gui.GuiPasswordField;
import de.paxii.clarinet.util.login.YggdrasilLoginBridge;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GuiDirectLogin extends GuiScreen {
	private final GuiScreen parentScreen;
	private final TimeManager timeManager;
	private GuiTextField userNameField;
	private GuiPasswordField passwordField;
	private String errorMessage;
	private boolean displayError;

	public GuiDirectLogin(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;

		this.timeManager = new TimeManager();
	}

	@Override
	public void initGui() {
		int startX = this.width / 2 - 100;
		int width = 200;

		this.userNameField = new GuiTextField(3, Wrapper.getFontRenderer(), startX, this.height / 2 - 50, width, 20);
		this.passwordField = new GuiPasswordField(5, Wrapper.getFontRenderer(), startX, this.height / 2 - 10, width, 20);

		this.buttonList.add(new GuiButton(0, this.width / 2 - 105, this.height - 25, 100, 20, "Cancel"));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 25, 100, 20, "Login"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		this.userNameField.drawTextBox();
		this.passwordField.drawTextBox();

		if (this.displayError) {
			this.drawCenteredString(Wrapper.getFontRenderer(), this.errorMessage, this.width / 2, 30, 0xff0000);

			if (this.timeManager.sleep(5000L)) {
				this.displayError = false;
			}
		} else {
			this.timeManager.updateLast();
		}

		this.drawCenteredString(Wrapper.getFontRenderer(), "Add Alt", this.width / 2, 5, 0xffffff);
		this.drawCenteredString(Wrapper.getFontRenderer(), "Username:", this.width / 2, this.height / 2 - 65, 0xffffff);
		this.drawCenteredString(Wrapper.getFontRenderer(), "Password:", this.width / 2, this.height / 2 - 25, 0xffffff);

		this.timeManager.updateTimer();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 15) {
			if (this.userNameField.isFocused()) {
				this.userNameField.setFocused(false);
				this.passwordField.setFocused(true);
			} else if (this.passwordField.isFocused()) {
				this.userNameField.setFocused(true);
				this.passwordField.setFocused(false);
			} else {
				this.userNameField.setFocused(true);
				this.passwordField.setFocused(false);
			}
		}

		this.userNameField.textboxKeyTyped(typedChar, keyCode);
		this.passwordField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
		} else if (button.id == 1) {
			String userName = this.userNameField.getText();
			String password = this.passwordField.getText();

			if (userName.length() == 0) {
				this.errorMessage = "Invalid Username!";
				this.displayError = true;
			} else if (userName.length() == 0 && password.length() == 0) {
				this.errorMessage = "Invalid Credentials!";
				this.displayError = true;
			} else {
				this.displayError = false;

				AltObject altObject = new AltObject(userName.contains("@") ? "" : userName, userName.contains("@") ? userName : "", password);

				if (YggdrasilLoginBridge.loginWithAlt(altObject) != null) {
					Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
				}
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
			throws IOException {
		this.userNameField.mouseClicked(mouseX, mouseY, mouseButton);
		this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}

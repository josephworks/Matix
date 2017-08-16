package de.paxii.clarinet.gui.menu.login;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.gui.GuiPasswordField;
import de.paxii.clarinet.util.login.YggdrasilLoginBridge;
import de.paxii.clarinet.util.notifications.NotificationPriority;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiDirectLogin extends GuiScreen {
  protected final GuiAltManager altManager;

  protected String displayTitle;

  protected List<GuiTextField> textFieldList;
  protected GuiTextField userNameField;
  protected GuiPasswordField passwordField;

  protected int buttonWidth;
  protected GuiButton loginButton;
  protected GuiButton cancelButton;

  public GuiDirectLogin(GuiAltManager altManager) {
    this.displayTitle = "Login";
    this.buttonWidth = 200;
    this.altManager = altManager;
    this.textFieldList = new ArrayList<>(3);
  }

  @Override
  public void initGui() {
    int startX = this.width / 2 - 100;

    this.textFieldList.add(
            this.userNameField = new GuiTextField(3, Wrapper.getFontRenderer(), startX, this.height / 2 - 50, this.buttonWidth, 20)
    );
    this.textFieldList.add(
            this.passwordField = new GuiPasswordField(5, Wrapper.getFontRenderer(), startX, this.height / 2 - 10, this.buttonWidth, 20)
    );

    this.buttonList.add(this.cancelButton = new GuiButton(0, this.width / 2 - 105, this.height - 25, 100, 20, "Cancel"));
    this.buttonList.add(this.loginButton = new GuiButton(1, this.width / 2 + 5, this.height - 25, 100, 20, "Login"));
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();

    this.drawCenteredString(Wrapper.getFontRenderer(), this.displayTitle, this.width / 2, 5, 0xffffff);
    this.textFieldList.forEach(GuiTextField::drawTextBox);
    this.drawTextFieldLabels();

    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  protected void drawTextFieldLabels() {
    this.drawCenteredString(Wrapper.getFontRenderer(), "Username:", this.width / 2, this.height / 2 - 65, 0xffffff);
    this.drawCenteredString(Wrapper.getFontRenderer(), "Password:", this.width / 2, this.height / 2 - 25, 0xffffff);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (keyCode == 15) {
      this.cycleSelection();
    }

    this.textFieldList.forEach(textField -> textField.textboxKeyTyped(typedChar, keyCode));
  }

  protected void cycleSelection() {
    if (this.userNameField.isFocused()) {
      this.setTextFieldFocused(this.passwordField);
    } else if (this.passwordField.isFocused()) {
      this.setTextFieldFocused(this.userNameField);
    } else {
      this.setTextFieldFocused(this.userNameField);
    }
  }

  protected void setTextFieldFocused(GuiTextField textField) {
    textField.setFocused(true);
    this.textFieldList.stream()
            .filter(textFieldInList -> textFieldInList != textField)
            .forEach(textFieldInList -> textFieldInList.setFocused(false));
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button == this.cancelButton) {
      Wrapper.getMinecraft().displayGuiScreen(this.altManager);
    } else if (button == this.loginButton) {
      String userName = this.userNameField.getText();
      String password = this.passwordField.getText();

      if (userName.length() == 0 && password.length() == 0) {
        Wrapper.getClient().getNotificationManager().addNotification(
                "Invalid Credentials", NotificationPriority.DANGER
        );
      } else {
        AltObject altObject = new AltObject(userName.contains("@") ? "" : userName, userName.contains("@") ? userName : "", password);
        if (YggdrasilLoginBridge.loginWithAlt(altObject) != null) {
          Wrapper.getMinecraft().displayGuiScreen(this.altManager);
        } else {
          Wrapper.getClient().getNotificationManager().addNotification(
                  "Invalid Credentials", NotificationPriority.DANGER
          );
        }
      }
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    this.textFieldList.forEach(textField -> textField.mouseClicked(mouseX, mouseY, mouseButton));
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }
}

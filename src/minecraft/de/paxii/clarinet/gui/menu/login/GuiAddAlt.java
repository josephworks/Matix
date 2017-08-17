package de.paxii.clarinet.gui.menu.login;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.notifications.NotificationPriority;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.util.ArrayList;

public class GuiAddAlt extends GuiDirectLogin {
  protected GuiTextField emailField;

  public GuiAddAlt(GuiAltManager altManager) {
    super(altManager);
    this.displayTitle = "Add Alt";
  }

  @Override
  public void initGui() {
    super.initGui();
    int startX = this.width / 2 - 100;

    this.passwordField.x = startX;
    this.passwordField.y = this.height / 2 + 30;
    this.textFieldList.add(
            this.emailField = new GuiTextField(4, Wrapper.getFontRenderer(), startX, this.height / 2 - 10, this.buttonWidth, 20)
    );
    this.loginButton.displayString = "Save";
  }

  @Override
  protected void drawTextFieldLabels() {
    this.drawCenteredString(Wrapper.getFontRenderer(), "Username:", this.width / 2, this.height / 2 - 65, 0xffffff);
    this.drawCenteredString(Wrapper.getFontRenderer(), "Email:", this.width / 2, this.height / 2 - 25, 0xffffff);
    this.drawCenteredString(Wrapper.getFontRenderer(), "Password:", this.width / 2, this.height / 2 + 15, 0xffffff);
  }

  @Override
  protected void cycleSelection() {
    if (this.userNameField.isFocused()) {
      this.setTextFieldFocused(this.emailField);
    } else if (this.emailField.isFocused()) {
      this.setTextFieldFocused(this.passwordField);
    } else {
      super.cycleSelection();
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button == this.loginButton) {
      String userName = this.userNameField.getText();
      String email = this.emailField.getText();
      String password = this.passwordField.getText();

      if (email.length() > 0 && password.length() == 0) {
        Wrapper.getClient().getNotificationManager().addNotification(
                "Invalid Password", NotificationPriority.DANGER
        );
      } else if (userName.length() == 0 && email.length() == 0 && password.length() == 0) {
        Wrapper.getClient().getNotificationManager().addNotification(
                "Invalid Credentials", NotificationPriority.DANGER
        );
      } else if (email.length() > 0 && !email.contains("@")) {
        Wrapper.getClient().getNotificationManager().addNotification(
                "Invalid Email", NotificationPriority.DANGER
        );
      } else {
        ArrayList<AltObject> altObjects = this.altManager.guiAltList.getAltObjects();
        altObjects.add(new AltObject(userName, email, password));
        this.altManager.setupGuiAltList(altObjects);
        this.altManager.initGui();
        Wrapper.getMinecraft().displayGuiScreen(this.altManager);
      }
    } else {
      super.actionPerformed(button);
    }
  }
}

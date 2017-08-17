package de.paxii.clarinet.gui.menu.login;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.gui.DisplayGuiScreenEvent;
import de.paxii.clarinet.gui.menu.hooks.GuiMainMenuHook;
import de.paxii.clarinet.util.alt.AltContainer;
import de.paxii.clarinet.util.file.FileService;
import de.paxii.clarinet.util.login.YggdrasilLoginBridge;
import de.paxii.clarinet.util.notifications.NotificationPriority;
import de.paxii.clarinet.util.settings.ClientSettings;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

public class GuiAltManager extends GuiScreen {

  private final GuiScreen parentScreen;

  private final GuiMainMenuHook mainMenuHook;

  private final File altFile;

  GuiAltList guiAltList;

  @Getter
  @Setter
  private GuiAltListEntry pressedSlot;

  public GuiAltManager(GuiScreen parentScreen) {
    this.parentScreen = parentScreen;

    if (this.parentScreen instanceof GuiMainMenuHook)
      this.mainMenuHook = (GuiMainMenuHook) this.parentScreen;
    else
      this.mainMenuHook = new GuiMainMenuHook();

    this.altFile = new File(ClientSettings.getClientFolderPath().getValue(), "alts.json");
    try {
      this.altFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.setupGuiAltList(this.loadAlts());

    Wrapper.getEventManager().register(this, DisplayGuiScreenEvent.class);
  }

  @EventHandler
  public void onDisplayGuiScreen(DisplayGuiScreenEvent event) {
    if (ClientSettings.getValue("client.hidden", Boolean.class))
      return;

    if (event.getGuiScreen() instanceof GuiMainMenu) {
      event.setGuiScreen(mainMenuHook);
    }
  }

  @Override
  public void initGui() {
    this.setupGuiAltList(this.guiAltList.getAltObjects());
    this.buttonList.clear();
    this.buttonList.add(new GuiButton(0, this.width / 2 - 50,
            this.height - 25, 100, 20, "Delete"));
    this.buttonList.add(new GuiButton(1, this.width / 2 - 160,
            this.height - 25, 100, 20, "Add"));
    this.buttonList.add(new GuiButton(2, this.width / 2 + 60,
            this.height - 25, 100, 20, "Done"));
    this.buttonList.add(new GuiButton(3, this.width / 2 - 50,
            this.height - 50, 100, 20, "Login"));
    this.buttonList.add(new GuiButton(4, this.width / 2 + 60,
            this.height - 50, 100, 20, "Direct"));
    this.buttonList.add(new GuiButton(5, this.width / 2 - 160,
            this.height - 50, 100, 20, "MCLeaks.net"));
  }

  public void setupGuiAltList(ArrayList<AltObject> altObjects) {
    this.guiAltList = new GuiAltList(this, altObjects);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (keyCode == 1) {
      Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();

    if (this.guiAltList != null) {
      this.guiAltList.drawScreen(mouseX, mouseY, partialTicks);
    }

    this.drawCenteredString(this.fontRenderer, "AltManager",
            this.width / 2, 8, 16777215);

    this.drawString(Wrapper.getFontRenderer(), "Username: ", 5, 8, 0xffffff);
    this.drawString(
            Wrapper.getFontRenderer(),
            Wrapper.getMinecraft().getSession().getUsername(),
            5 + Wrapper.getFontRenderer().getStringWidth("Username: "),
            8,
            0x00ff00
    );

    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 0 && this.pressedSlot != null) {
      this.guiAltList.getEntryList().remove(this.getPressedSlot());
    } else if (button.id == 1) {
      Wrapper.getMinecraft().displayGuiScreen(new GuiAddAlt(this));
    } else if (button.id == 2) {
      Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
    } else if (button.id == 3 && this.pressedSlot != null) {
      if (YggdrasilLoginBridge.loginWithAlt(this.getPressedSlot().getAlt()) == null) {
        Wrapper.getClient().getNotificationManager().addNotification(
                "Invalid Credentials!", NotificationPriority.DANGER
        );
      }
    } else if (button.id == 4) {
      Wrapper.getMinecraft().displayGuiScreen(new GuiDirectLogin(this));
    } else if (button.id == 5) {
      Wrapper.getMinecraft().displayGuiScreen(new GuiMcLeaksLogin(this));
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.guiAltList.handleMouseInput();
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    this.guiAltList.mouseClicked(mouseX, mouseY, mouseButton);
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    this.guiAltList.mouseReleased(mouseX, mouseY, state);
    super.mouseReleased(mouseX, mouseY, state);
  }

  @Override
  public void onGuiClosed() {
    this.saveAlts();
  }

  private ArrayList<AltObject> loadAlts() {
    ArrayList<AltObject> altObjects = new ArrayList<>(10);
    try {
      AltContainer altContainer = FileService.getFileContents(this.altFile, AltContainer.class);
      if (altContainer != null) {
        altObjects = altContainer.getAltList().stream()
                .sorted()
                .peek(alt -> alt.setPassword(Wrapper.getStringEncryption().decryptString(alt.getPassword())))
                .collect(Collectors.toCollection(ArrayList::new));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return altObjects;
  }

  private void saveAlts() {
    try {
      if (this.altFile.exists() && !this.altFile.delete()) {
        return;
      }

      AltContainer altContainer = new AltContainer(this.guiAltList.getAltObjects().stream()
              .map(AltObject::copy)
              .peek(alt -> alt.setPassword(Wrapper.getStringEncryption().encryptString(alt.getPassword())))
              .collect(Collectors.toCollection(ArrayList::new))
      );
      FileService.setFileContentsAsJson(this.altFile, altContainer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

package de.paxii.clarinet.gui.menu.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.gui.DisplayGuiScreenEvent;
import de.paxii.clarinet.gui.menu.hooks.GuiMainMenuHook;
import de.paxii.clarinet.util.alt.AltContainer;
import de.paxii.clarinet.util.login.YggdrasilLoginBridge;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.threads.ConcurrentArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import lombok.Getter;
import lombok.Setter;

public class GuiAltManager extends GuiScreen {
  private final GuiScreen parentScreen;
  private final GuiMainMenuHook mainMenuHook;
  private final File altFile;
  private GuiAltList guiAltList;
  @Getter
  @Setter
  private GuiAltListEntry pressedSlot;
  @Getter
  private ConcurrentArrayList<AltObject> altList;

  public GuiAltManager(GuiScreen parentScreen) {
    this.parentScreen = parentScreen;
    this.altList = new ConcurrentArrayList<>();

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
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (keyCode == 1) {
      Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
    }
  }

  @Override
  public void initGui() {
    this.loadAlts();

    this.guiAltList = new GuiAltList(this);

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

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();

    if (this.guiAltList != null) {
      this.guiAltList.drawScreen(mouseX, mouseY, partialTicks);
    }

    this.drawCenteredString(this.fontRendererObj, "AltManager",
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
    if (button.id == 0) {
      if (this.pressedSlot != null) {
        this.getAltList().remove(this.getPressedSlot().getAlt());
        this.saveAlts();

        new Thread(() -> {
          try {
            Thread.sleep(150L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          GuiAltManager.this.initGui();
        }).start();
      }
    } else if (button.id == 1) {
      Wrapper.getMinecraft().displayGuiScreen(new GuiAddAlt(this));
    } else if (button.id == 2) {
      Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
    } else if (button.id == 3) {
      if (this.pressedSlot != null) {
        YggdrasilLoginBridge.loginWithAlt(this.getPressedSlot().getAlt());
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
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
          throws IOException {
    this.guiAltList.mouseClicked(mouseX, mouseY, mouseButton);

    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    this.guiAltList.mouseReleased(mouseX, mouseY, state);
    super.mouseReleased(mouseX, mouseY, state);
  }

  private void loadAlts() {
    this.altList.clear();

    try {
      Gson gson = new Gson();

      if (this.altFile.exists()) {
        BufferedReader br = new BufferedReader(new FileReader(
                this.altFile));

        String line, jsonString = "";

        while ((line = br.readLine()) != null) {
          jsonString += line;
        }

        AltContainer altContainer = gson.fromJson(jsonString, AltContainer.class);

        if (altContainer != null) {
          altContainer.getAltList().forEach((alt) -> {
            alt.setPassword((alt.getPassword().length() > 0) ? Wrapper.getStringEncryption().decryptString(alt.getPassword()) : "");

            this.altList.add(alt);
          });
        }

        br.close();
        Collections.sort(this.altList);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void saveAlts() {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      if (this.altFile.exists() && !this.altFile.delete()) {
        return;
      }

      if (this.altFile.createNewFile()) {
        FileWriter fw = new FileWriter(this.altFile);
        AltContainer altContainer = new AltContainer(this.altList);

        altContainer.getAltList().forEach((alt) -> alt.setPassword((alt.getPassword().length() > 0) ? Wrapper.getStringEncryption().encryptString(alt.getPassword()) : ""));

        String jsonString = gson.toJson(altContainer);
        fw.write(jsonString);

        fw.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

package de.paxii.clarinet.gui.menu.hooks;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.menu.login.GuiAltManager;
import de.paxii.clarinet.gui.menu.store.module.GuiModuleStore;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;

import java.io.IOException;

public class GuiMainMenuHook extends GuiMainMenu {

  @Override
  public void initGui() {
    super.initGui();

    this.buttonList.add(new GuiButton(200, 2, this.height - 40, 100, 20, "AltManager"));
    this.buttonList.add(new GuiButton(201, this.width - 102, this.height - 40, 100, 20, "Plugins"));
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);

    if (button.id == 200) {
      Wrapper.getMinecraft().displayGuiScreen(new GuiAltManager(this));
    }
    if (button.id == 201) {
      Wrapper.getMinecraft().displayGuiScreen(new GuiModuleStore(this));
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);

    Wrapper.getFontRenderer().drawString(Client.getClientName() + " " + Client.getClientVersion(), 2, 2, 0xFFFFFFFF);
  }
}

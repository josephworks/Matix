package de.paxii.clarinet.gui.menu.hooks;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.menu.login.GuiAltManager;
import de.paxii.clarinet.gui.menu.store.module.GuiModuleStore;
import de.paxii.clarinet.util.protocol.ProtocolVersion;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;

import java.io.IOException;

public class GuiMainMenuHook extends GuiMainMenu {

  private GuiButton pluginsButton;
  private GuiButton protocolButton;

  @Override
  public void initGui() {
    super.initGui();

    this.buttonList.add(new GuiButton(200, 2, this.height - 40, 100, 20, "AltManager"));
    this.pluginsButton = new GuiButton(201, this.width - 102, this.height - 40, 100, 20, "Plugins");
    this.protocolButton = new GuiButton(202, 2, this.height - 65, 100, 20, ProtocolVersion.getGameVersion());

//    this.pluginsButton.enabled = false;
    this.buttonList.add(pluginsButton);

    if (ProtocolVersion.CompatibleVersion.values().length > 1) {
      this.buttonList.add(protocolButton);
    }
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
    if (button.id == this.protocolButton.id) {
      ProtocolVersion.cycleVersion();
      this.protocolButton.displayString = ProtocolVersion.getGameVersion();
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);

    Wrapper.getFontRenderer().drawString(Client.getClientName() + " " + Client.getClientVersion(), 2, 2, 0xFFFFFFFF);
  }
}

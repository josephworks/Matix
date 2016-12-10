package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.gui.ingame.ClientClickableGui;
import de.paxii.clarinet.gui.ingame.panel.theme.IClientTheme;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.Chat;

import org.lwjgl.input.Keyboard;

public class ModuleClickGui extends Module {
  public ModuleClickGui() {
    super("ClickGui", ModuleCategory.RENDER, Keyboard.KEY_RSHIFT);

    this.setDisplayedInGui(false);
    this.setCommand(true);
    this.setDescription("GUI Related Commands");
    this.setSyntax("clickgui theme <Name/List>");

    Wrapper.getEventManager().register(this);
  }

  @Override
  public void onEnable() {
    Wrapper.getMinecraft().displayGuiScreen(Wrapper.getClickableGui());
  }

  @EventHandler
  public void onTick(IngameTickEvent event) {
    if (!(Wrapper.getMinecraft().currentScreen instanceof ClientClickableGui)) {
      if (this.isEnabled()) {
        this.setEnabled(false);
      } else {
        Wrapper.getClickableGui().getGuiPanels().forEach(guiPanel -> {
          if (guiPanel.isPinned()) {
            guiPanel.drawPanel(0, 0);
          }
        });
      }
    }
  }

  @Override
  public void onCommand(String[] args) {
    if (args.length >= 2) {
      if (args[0].equalsIgnoreCase("theme")) {
        if (args[1].equalsIgnoreCase("list")) {
          String themes = "";
          for (IClientTheme clientTheme : Wrapper.getClickableGui().getPanelThemes()) {
            themes += ", " + clientTheme.getName();
          }

          if (themes.length() > 2) {
            themes = themes.substring(2);
          }

          Chat.printClientMessage("Available Themes: " + themes);
        } else {
          String themeName = args[1];


          if (Wrapper.getClickableGui().doesThemeExist(themeName)) {
            IClientTheme clientTheme = Wrapper.getClickableGui().getTheme(themeName);
            Wrapper.getClickableGui().setCurrentTheme(clientTheme);

            Chat.printClientMessage("The GUI Theme was set to " + clientTheme.getName() + ".");
          } else {
            Chat.printClientMessage("There is no such Theme!");
          }
        }
      } else {
        Chat.printClientMessage("Unknown subcommand!");
      }
    } else {
      Chat.printClientMessage("Too few Arguments!");
    }
  }
}

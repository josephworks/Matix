package de.paxii.clarinet.command.commands.module;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.gui.DisplayGuiScreenEvent;
import de.paxii.clarinet.gui.menu.controls.GuiKeybinds;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.Chat;

import net.minecraft.client.gui.GuiChat;

import org.lwjgl.input.Keyboard;

public class CommandKey extends AClientCommand {

  private boolean openGui;
  private final GuiKeybinds guiKeybinds;

  public CommandKey() {
    this.guiKeybinds = new GuiKeybinds(null);
    Wrapper.getEventManager().register(this, DisplayGuiScreenEvent.class);
  }

  @Override
  public String getCommand() {
    return "key";
  }

  @Override
  public String getDescription() {
    return "Keybind Management";
  }

  @Override
  public void runCommand(String[] args) {
    if (args.length > 0) {
      if (args.length >= 2) {
        String moduleName = args[1];

        if (args[0].equalsIgnoreCase("set")) {
          if (!Wrapper.getModuleManager().doesModuleExist(moduleName)) {
            Chat.printClientMessage("Unknown Module!");
            return;
          }

          Module module = Wrapper.getModuleManager().getModuleIgnoreCase(moduleName);

          if (args.length >= 3) {
            String keyString = args[2].toUpperCase();

            if (keyString.equals("."))
              keyString = "PERIOD";
            if (keyString.equals(","))
              keyString = "COMMA";
            if (keyString.equals("-"))
              keyString = "MINUS";
            if (keyString.equals("+"))
              keyString = "PLUS";

            int moduleKey = Keyboard.getKeyIndex(keyString);
            module.setKey(moduleKey);
            Chat.printClientMessage(String.format("%s Key has been set to %s.", module, Keyboard.getKeyName(moduleKey)));
          } else {
            Chat.printClientMessage(module + " Key has been removed!");
          }
        } else if (args[0].equalsIgnoreCase("remove")) {
          if (!Wrapper.getModuleManager().doesModuleExist(moduleName)) {
            Chat.printClientMessage("Unknown Module!");
            return;
          }

          Module module = Wrapper.getModuleManager().getModuleIgnoreCase(moduleName);
          module.setKey(-1);

          Chat.printClientMessage(module + " Key has been removed!");
        } else {
          Chat.printClientMessage("Unknown sub-command!");
        }
      } else {
        if (args[0].equalsIgnoreCase("gui")) {
          this.openGui = true;
        } else {
          Chat.printClientMessage("Unknown sub-command!");
        }
      }
    } else {
      Chat.printClientMessage("Too few arguments!");
    }
  }

  @EventHandler
  public void onDisplayGuiScreen(DisplayGuiScreenEvent screenEvent) {
    if (this.openGui && Wrapper.getMinecraft().currentScreen instanceof GuiChat && screenEvent.getGuiScreen() == null) {
      screenEvent.setGuiScreen(this.guiKeybinds);
      this.openGui = false;
    }
  }

  @Override
  public String getUsage() {
    return "key <set/remove/gui> <module> [key]";
  }

  @Override
  public CommandCategory getCategory() {
    return CommandCategory.MODULE;
  }
}

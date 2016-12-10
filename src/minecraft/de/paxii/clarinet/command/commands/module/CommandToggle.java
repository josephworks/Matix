package de.paxii.clarinet.command.commands.module;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.chat.ChatColor;

public class CommandToggle extends AClientCommand {
  @Override
  public String getCommand() {
    return "toggle";
  }

  @Override
  public String getDescription() {
    return "Toggles the Module given as argument";
  }

  @Override
  public void runCommand(String[] args) {
    if (args.length > 0) {
      String moduleName = "";

      if (args.length == 1) {
        moduleName = args[0];
      } else {
        for (String arg : args) {
          moduleName += arg;
          moduleName += " ";
        }
      }

      while (moduleName.endsWith(" ") && moduleName.length() != 1) {
        moduleName = moduleName.substring(0, moduleName.length() - 1);
      }

      if (Wrapper.getModuleManager().doesModuleExist(moduleName)) {
        Module module = Wrapper.getModuleManager().getModuleIgnoreCase(
                moduleName);
        module.toggle();
        Chat.printClientMessage(module.getName() + " has been " + (module.isEnabled() ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled") + ChatColor.WHITE + ".");
      } else {
        Chat.printClientMessage("Toggle: Unknown Module!");
      }
    } else {
      Chat.printClientMessage(String.format("Toggle: No Modulename specified! (%s)", this.getUsage()));
    }
  }

  @Override
  public String getUsage() {
    return getCommand() + " <module>";
  }

  @Override
  public CommandCategory getCategory() {
    return CommandCategory.MODULE;
  }
}

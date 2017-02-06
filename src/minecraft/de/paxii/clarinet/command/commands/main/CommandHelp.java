package de.paxii.clarinet.command.commands.main;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.chat.ChatColor;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.Map.Entry;
import java.util.TreeMap;

public class CommandHelp extends AClientCommand {

  @Override
  public String getCommand() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "Command Help";
  }

  @Override
  public void runCommand(String[] args) {
    if (args.length == 0) {
      TreeMap<String, Module> sortedModules = new TreeMap<>(Wrapper.getModuleManager().getModuleList());

      Chat.printClientMessage("--------------------------------------------");
      Chat.printClientMessage("Module commands:");

      for (Entry<String, Module> moduleEntry : sortedModules.entrySet()) {
        Module module = moduleEntry.getValue();

        if (module.isCommand()) {
          Chat.printClientMessage(module.getName() + ": " + module.getDescription());
        }
      }

      TreeMap<String, AClientCommand> sortedCommands = new TreeMap<>(Wrapper.getConsole().getCommandList());

      Chat.printClientMessage("--------------------------------------------");
      Chat.printClientMessage("Commands:");

      for (Entry<String, AClientCommand> commandEntry : sortedCommands.entrySet()) {
        AClientCommand command = commandEntry.getValue();
        Chat.printClientMessage(command.getCommand() + ": " + command.getDescription());
      }

      Chat.printClientMessage("--------------------------------------------");
    } else {
      String searchString = args[0].toLowerCase();
      if (Wrapper.getConsole().getCommandList().containsKey(searchString)) {
        AClientCommand command = Wrapper.getConsole().getCommandList().get(searchString);

        Chat.printClientMessage("--------------------------------------------");
        Chat.printClientMessage(command.getCommand() + " Help:");
        Chat.printClientMessage("Description: " + command.getDescription());
        Chat.printClientMessage("Syntax: " + command.getUsage());
      } else if (Wrapper.getModuleManager().doesModuleExist(searchString)) {
        Module module = Wrapper.getModuleManager().getModuleIgnoreCase(searchString);

        Chat.printClientMessage("--------------------------------------------");
        Chat.printClientMessage(module.getName() + " Help:");
        Chat.printClientMessage("Description: " + module.getDescription());
        if (module.isCommand()) {
          Chat.printClientMessage("Syntax: " + module.getSyntax());
        }

        IChatComponent textComponent = new ChatComponentText(Chat.getPrefix() + "Documentation: ");
        IChatComponent urlComponent = new ChatComponentText("Click here");
        ChatStyle chatStyle = new ChatStyle();
        chatStyle.setUnderlined(true);
        chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, module.getHelpUrl()));
        urlComponent.setChatStyle(chatStyle);

        textComponent.appendSibling(urlComponent);
        Chat.printChatComponent(textComponent);

        if (module.isPlugin()) {
          Chat.printClientMessage(ChatColor.RED + "Warning: This Module is a Plugin. Documentation URL might be incorrect!");
        }
      } else {
        Chat.printClientMessage(String.format("Could not find the command \"%s\"!", args[0]));
      }
    }
  }

  @Override
  public String getUsage() {
    return "help [command/module]";
  }

  @Override
  public CommandCategory getCategory() {
    return CommandCategory.MAIN;
  }

}

package de.paxii.clarinet.command.commands.main;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.event.EventManager;
import de.paxii.clarinet.event.events.client.PostReloadClientEvent;
import de.paxii.clarinet.event.events.client.PreReloadClientEvent;
import de.paxii.clarinet.util.chat.Chat;

import java.util.ArrayList;

public class CommandReload extends AClientCommand {

  @Override
  public String getCommand() {
    return "reload";
  }

  @Override
  public String getDescription() {
    return "Searches for new Modules and Commands and loads them";
  }

  @Override
  public void runCommand(String[] args) {
    PreReloadClientEvent preReloadClientEvent = new PreReloadClientEvent();
    EventManager.call(preReloadClientEvent);

    if (preReloadClientEvent.isCancelled()) {
      Chat.printClientMessage("Client reload has been cancelled!");

      return;
    }

    final ArrayList<String> oldModules = new ArrayList<>();
    final ArrayList<String> oldCommands = new ArrayList<>();

    Wrapper.getModuleManager().getModuleList().forEach((k, m) -> oldModules.add(k));
    Wrapper.getConsole().getCommandList().forEach((k, c) -> oldCommands.add(k));

    Chat.printClientMessage("Reloading Client...");
    Wrapper.getModuleManager().getExternalModuleLoader().loadModules(() -> {
      boolean somethingNew = false;

      if (oldModules.size() != Wrapper.getModuleManager().getModuleList().size()) {
        somethingNew = true;
        Wrapper.getModuleManager().getModuleList().forEach((k, m) -> {
          if (!oldModules.contains(k)) {
            m.onStartup();
            Chat.printClientMessage("New Module: " + k);
          }
        });
      }

      if (oldCommands.size() != Wrapper.getConsole().getCommandList().size()) {
        somethingNew = true;
        Wrapper.getConsole().getCommandList().forEach((k, c) -> {
          if (!oldCommands.contains(k))
            Chat.printClientMessage("New Command: " + k);
        });
      } else {
        if (!somethingNew) {
          somethingNew = false;
        }
      }

      if (!somethingNew) {
        Chat.printClientMessage("No new Modules found!");
      } else {
        Wrapper.getClickableGui().loadPanels();
      }

      PostReloadClientEvent postReloadClientEvent = new PostReloadClientEvent();
      EventManager.call(postReloadClientEvent);
    });
  }

  @Override
  public String getUsage() {
    return getCommand();
  }

  @Override
  public CommandCategory getCategory() {
    return CommandCategory.MAIN;
  }

}

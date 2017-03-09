package de.paxii.clarinet.command.commands.main;

import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.player.capesapi.CapesApi;

/**
 * Created by Lars on 09.03.2017.
 */
public class CommandResetCapes extends AClientCommand {
  @Override
  public String getCommand() {
    return "reloadcapes";
  }

  @Override
  public String getDescription() {
    return "Reload Capes";
  }

  @Override
  public void runCommand(String[] args) {
    CapesApi.reset();
    Chat.printClientMessage("Capes were reloaded.");
  }

  @Override
  public String getUsage() {
    return null;
  }

  @Override
  public CommandCategory getCategory() {
    return null;
  }
}

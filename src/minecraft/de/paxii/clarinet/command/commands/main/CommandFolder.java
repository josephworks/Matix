package de.paxii.clarinet.command.commands.main;

import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Lars on 04.08.2016.
 */
public class CommandFolder extends AClientCommand {
  @Override
  public String getCommand() {
    return "folder";
  }

  @Override
  public String getDescription() {
    return "Opens the Matix Base Folder";
  }

  @Override
  public void runCommand(String[] args) {
    String relativePath = ClientSettings.getClientFolderPath().getValue();
    if (relativePath.startsWith("./")) {
      relativePath = relativePath.substring(2);
    }
    URI uri = new File(relativePath).toURI();
    try {
      this.openWebLink(uri);
      Chat.printClientMessage("Opened \"" + relativePath + "\".");
    } catch (IOException ioException) {
      Chat.printClientMessage("Couldn't open \"" + relativePath + "\".");
    }
  }

  private void openWebLink(URI url) throws IOException {
    java.awt.Desktop.getDesktop().browse(url);
  }

  @Override
  public String getUsage() {
    return "folder";
  }

  @Override
  public CommandCategory getCategory() {
    return CommandCategory.MAIN;
  }
}

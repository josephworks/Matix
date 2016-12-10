package de.paxii.clarinet.command;

public interface IClientCommand {
  String getCommand();

  String getDescription();

  void runCommand(String[] args);

  String getUsage();

  CommandCategory getCategory();
}

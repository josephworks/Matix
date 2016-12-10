package de.paxii.clarinet.command;

public abstract class AClientCommand implements IClientCommand, Comparable<AClientCommand> {
  @Override
  public int compareTo(AClientCommand command) {
    return this.getCommand().compareToIgnoreCase(command.getCommand());
  }
}

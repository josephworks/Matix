package de.paxii.clarinet.command.commands.main;

import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.util.chat.Chat;

public class CommandSay extends AClientCommand {

	@Override
	public String getCommand() {
		return "say";
	}

	@Override
	public String getDescription() {
		return "Send a Message to the Server";
	}

	@Override
	public void runCommand(String[] args) {
		if (args.length > 0) {
			StringBuilder stringBuilder = new StringBuilder();

			for (String arg : args) {
				stringBuilder.append(arg).append(" ");
			}

			String chatMessage = stringBuilder.toString().trim();

			Chat.sendChatMessage(chatMessage);
		}
	}

	@Override
	public String getUsage() {
		return "say <message>";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MAIN;
	}

}

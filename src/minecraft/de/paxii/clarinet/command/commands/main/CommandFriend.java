package de.paxii.clarinet.command.commands.main;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.util.chat.Chat;

import java.util.Map.Entry;

public class CommandFriend extends AClientCommand {

	@Override
	public String getCommand() {
		return "friend";
	}

	@Override
	public String getDescription() {
		return "Manage friends";
	}

	@Override
	public void runCommand(String[] args) {
		if (args.length > 0) {
			if (args.length >= 2) {
				if (args[0].equalsIgnoreCase("add")) {
					String friendName = args[1];
					String colorString = "";

					if (args.length >= 3) {
						colorString = args[2];
					}
					int friendColor = -1;

					try {
						if (colorString.startsWith("0x"))
							colorString = colorString.substring(2);

						friendColor = Integer.parseInt(colorString, 16);
					} catch (Exception x) {
					}

					if (friendColor != -1) {
						Wrapper.getFriendManager().addFriend(friendName, friendColor);
						Chat.printClientMessage("Added Friend " + friendName + " (0x" + Integer.toHexString(friendColor) + ").");
					} else {
						Wrapper.getFriendManager().addFriend(friendName);
						Chat.printClientMessage("Added Friend " + friendName + ".");
					}
				} else if (args[0].equalsIgnoreCase("remove")) {
					String friendName = args[1];

					Wrapper.getFriendManager().removeFriend(friendName);
					Chat.printClientMessage("Removed Friend " + friendName + ".");
				} else {
					Chat.printClientMessage("Unknown subcommand");
				}
			} else if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("list")) {
					Chat.printClientMessage("Friend list:");
					for (Entry<String, Integer> friend : Wrapper.getFriendManager().getFriends().entrySet()) {
						Chat.printClientMessage("Friend: " + friend.getKey() + " (0x" + Integer.toHexString(friend.getValue()) + ").");
					}
				} else if (args[0].equalsIgnoreCase("clear")) {
					Wrapper.getFriendManager().getFriends().clear();

					Chat.printClientMessage("Friend list has been cleared!");
				}
			}
		} else {
			Chat.printClientMessage("Too few arguments!");
		}
	}

	@Override
	public String getUsage() {
		return "friend [add/remove] [friend]";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MAIN;
	}

}

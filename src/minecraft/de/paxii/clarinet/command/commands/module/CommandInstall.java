package de.paxii.clarinet.command.commands.module;

import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.store.ModuleStore;

/**
 * Created by Lars on 07.02.2016.
 */
public class CommandInstall extends AClientCommand {
	@Override
	public String getCommand() {
		return "install";
	}

	@Override
	public String getDescription() {
		return "Downloads and Installs an external Module hosted on http://paxii.de";
	}

	@Override
	public void runCommand(String[] args) {
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("list")) {
				Chat.printClientMessage("Available external Modules:");
				Chat.printChatMessage("-----------------------------------------------------");

				ModuleStore.listModules().values().forEach((moduleEntry -> {
					Chat.printClientMessage("Module: " + moduleEntry.getModule());
					Chat.printClientMessage("Description: " + moduleEntry.getDescription());
					Chat.printClientMessage("Version: " + moduleEntry.getVersion());
					Chat.printChatMessage("-----------------------------------------------------");
				}));
			} else {
				Chat.printClientMessage(String.format("Downloading Module %s", args[0]));

				ModuleStore.downloadModule(args[0]);
			}
		}
	}

	@Override
	public String getUsage() {
		return "install <name/list>";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MODULE;
	}
}

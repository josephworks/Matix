package de.paxii.clarinet.command.commands.main;

import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.settings.ClientSetting;
import de.paxii.clarinet.util.settings.ClientSettings;

/**
 * Created by Lars on 02.08.2016.
 */
public class CommandSetting extends AClientCommand {
	@Override
	public String getCommand() {
		return "setting";
	}

	@Override
	public String getDescription() {
		return "Allows you to edit client settings";
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public void runCommand(String[] args) {
		if (args.length >= 2) {
			String settingKey = args[1];

			if (args[0].equalsIgnoreCase("get")) {
				ClientSetting clientSetting = ClientSettings.getClientSettings().getOrDefault(settingKey, null);

				if (clientSetting != null) {
					Chat.printClientMessage("Client Setting \"" + clientSetting.getName() + "\" is currently set to \"" + clientSetting.getValue() + "\".");
				}
			} else if (args[0].equalsIgnoreCase("set")) {
				if (args.length >= 3) {
					ClientSetting clientSetting = ClientSettings.getClientSettings().getOrDefault(settingKey, null);

					if (clientSetting != null) {
						String value = args[2];

						try {
							Class type = clientSetting.getValue().getClass();
							Object settingValue;

							if (type == Boolean.class) {
								settingValue = Boolean.parseBoolean(value);
							} else if (type == Integer.class) {
								settingValue = Integer.parseInt(value);
							} else if (type == Double.class) {
								settingValue = Double.parseDouble(value);
							} else if (type == String.class) {
								settingValue = value;
							} else {
								Chat.printClientMessage("Setting Type was not recognized. Please report this.");
								return;
							}

							clientSetting.setValue(settingValue);
							Chat.printClientMessage("Client Setting \"" + clientSetting.getName() + "\" has been set to \"" + clientSetting.getValue() + "\".");
						} catch (Exception x) {
							x.printStackTrace();
						}
					} else {
						Chat.printClientMessage(String.format("No Client Setting with key \"%s\" found.", settingKey));
					}
				} else {
					Chat.printClientMessage("Too few arguments!");
				}
			} else {
				Chat.printClientMessage("Unknown subcommand!");
			}

		}
	}

	@Override
	public String getUsage() {
		return "setting <client setting key> <value>";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MAIN;
	}
}

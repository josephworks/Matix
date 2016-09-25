package de.paxii.clarinet.command.commands.module;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.settings.ValueBase;

import java.text.NumberFormat;

/**
 * Created by Lars on 25.09.2016.
 */
public class CommandValue extends AClientCommand {
	@Override
	public String getCommand() {
		return "value";
	}

	@Override
	public String getDescription() {
		return "Allows you to list and modify values of installed modules.";
	}

	@Override
	public void runCommand(String[] args) {
		if (args.length >= 2) {
			if (Wrapper.getModuleManager().doesModuleExist(args[1])) {
				Module module = Wrapper.getModuleManager().getModuleIgnoreCase(args[1]);

				if (args[0].equals("list")) {
					Chat.printClientMessage(String.format("Values of Module \"%s\":", module.getName()));
					module.getModuleValues().values().forEach(value -> {
						String valueString = NumberFormat.getNumberInstance().format(value.getValue());
						Chat.printClientMessage(value.getName() + ": " + valueString);
					});
				} else if (args.length >= 3) {
					final ValueBase[] output = { null };
					module.getModuleValues().forEach((valueKey, valueBase) -> {
						if (valueKey.equalsIgnoreCase(args[2])
								|| valueBase.getName().equalsIgnoreCase(args[2])
								|| valueBase.getDisplayName().equalsIgnoreCase(args[2])) {
							output[0] = valueBase;
						}
					});

					if (output[0] != null) {
						NumberFormat numberFormat = NumberFormat.getNumberInstance();

						if (args[0].equalsIgnoreCase("get")) {
							Chat.printClientMessage(String.format("Value \"%s\" of Module \"%s\".", output[0].getDisplayName(), module.getName()));
							Chat.printClientMessage("Current Value: " + numberFormat.format(output[0].getValue()));
							Chat.printClientMessage("Min Value: " + numberFormat.format(output[0].getMin()));
							Chat.printClientMessage("Max Value: " + numberFormat.format(output[0].getMax()));
						} else if (args[0].equalsIgnoreCase("set")) {
							if (args.length >= 4) {
								try {
									float newValue = Float.parseFloat(args[3]);
									output[0].setValue(newValue);

									Chat.printClientMessage(String.format(
											"Value \"%s\" of Module \"%s\" was set to \"%s\".",
											output[0].getDisplayName(),
											module.getName(),
											numberFormat.format(newValue)
									));
								} catch (NumberFormatException e) {
									Chat.printClientMessage(String.format("\"%s\" is not a valid value.", args[3]));
								}
							} else {
								Chat.printClientMessage("Too few arguments.");
							}
						} else {
							Chat.printClientMessage("Unkown subcommand.");
						}
					} else {
						Chat.printClientMessage(String.format("Value \"%s\" was not found in Module \"%s\".", args[2], module.getName()));
					}
				} else {
					Chat.printClientMessage("Too few arguments.");
				}
			} else {
				Chat.printClientMessage(String.format("Module \"%s\" is not installed.", args[1]));
			}
		} else {
			Chat.printClientMessage("Too few arguments.");
		}
	}

	@Override
	public String getUsage() {
		return "value <get/set/list> <module> <value name> <new value>";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MAIN;
	}
}

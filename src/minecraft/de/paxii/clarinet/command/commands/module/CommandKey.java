package de.paxii.clarinet.command.commands.module;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.gui.menu.controls.GuiKeybinds;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.Chat;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class CommandKey extends AClientCommand {

	@Override
	public String getCommand() {
		return "key";
	}

	@Override
	public String getDescription() {
		return "Keybind Management";
	}

	@Override
	public void runCommand(String[] args) {
		if (args.length > 0) {
			if (args.length >= 2) {
				String moduleName = args[1];

				if (args[0].equalsIgnoreCase("set")) {
					if (!Wrapper.getModuleManager().doesModuleExist(moduleName)) {
						Chat.printClientMessage("Unknown Module!");
						return;
					}

					Module module = Wrapper.getModuleManager().getModuleIgnoreCase(moduleName);

					if (args.length >= 3) {
						String keyString = args[2].toUpperCase();

						if (keyString.equals("."))
							keyString = "PERIOD";
						if (keyString.equals(","))
							keyString = "COMMA";
						if (keyString.equals("-"))
							keyString = "MINUS";
						if (keyString.equals("+"))
							keyString = "PLUS";

						int moduleKey = Keyboard.getKeyIndex(keyString);
						module.setKey(moduleKey);
						Chat.printClientMessage(String.format("%s Key has been set to %s.", module, Keyboard.getKeyName(moduleKey)));
					} else {
						Chat.printClientMessage(module + " Key has been removed!");
					}
				} else if (args[0].equalsIgnoreCase("remove")) {
					if (!Wrapper.getModuleManager().doesModuleExist(moduleName)) {
						Chat.printClientMessage("Unknown Module!");
						return;
					}

					Module module = Wrapper.getModuleManager().getModuleIgnoreCase(moduleName);
					module.setKey(-1);

					Chat.printClientMessage(module + " Key has been removed!");
				} else {
					Chat.printClientMessage("Unknown sub-command!");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("gui")) {
					new Thread(() -> {
						try {
							Thread.sleep(200L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							Wrapper.getMinecraft().displayGuiScreen(new GuiKeybinds(null));
							//TODO: Client Eh what?
							// This prevents the Cursor from being invisible
							// Find some other way, this is ugly.
							Mouse.setCursorPosition(-999, -999);
							Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
						}
					}).start();
				} else {
					Chat.printClientMessage("Unknown sub-command!");
				}
			} else {
				Chat.printClientMessage("Too few arguments!");
			}
		} else {
			Chat.printClientMessage("Too few arguments!");
		}
	}

	@Override
	public String getUsage() {
		return "key <set/remove/gui> <module> [key]";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MODULE;
	}
}

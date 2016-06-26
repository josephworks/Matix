package de.paxii.clarinet.command;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.EventPriority;
import de.paxii.clarinet.event.events.game.StopGameEvent;
import de.paxii.clarinet.event.events.gui.DisplayGuiScreenEvent;
import de.paxii.clarinet.event.events.player.PlayerSendChatMessageEvent;
import de.paxii.clarinet.gui.menu.chat.GuiChatConsole;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.settings.ClientSettings;
import lombok.Getter;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiSleepMP;
import org.reflections.Reflections;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class ClientConsole {
	@Getter
	private TreeMap<String, AClientCommand> commandList = new TreeMap<>();

	public ClientConsole() {
		addCommands();
		Wrapper.getEventManager().register(this);
	}

	public void reloadCommands() {
		addCommands();
	}

	private void addCommands() {
		for (CommandCategory ca : CommandCategory.values()) {
			String packageName = "de.paxii.clarinet.command.commands."
					+ ca.toString().toLowerCase();
			System.out.println("Searching for Commands in " + packageName);

			Reflections reflections = new Reflections(packageName);

			Set<Class<? extends AClientCommand>> allClasses = reflections
					.getSubTypesOf(AClientCommand.class);

			for (Class<? extends AClientCommand> c : allClasses) {
				try {
					AClientCommand newCommand = c.newInstance();
					addCommand(newCommand);
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
		}
	}

	public void addCommand(AClientCommand command) {
		if (!commandList.containsKey(command.getCommand())) {
			System.out.println("Loading " + command.getCommand() + "...");
			commandList.put(command.getCommand(), command);
		}
	}

	public void removeCommand(IClientCommand command) {
		if (commandList.containsKey(command.getCommand())) {
			commandList.remove(command.getCommand());
		}
	}

	@EventHandler
	public void onChatMessage(PlayerSendChatMessageEvent event) {
		String chatMessage = event.getChatMessage();
		String clientPrefix = ClientSettings.getValue("client.prefix", String.class);

		if (chatMessage.startsWith(clientPrefix)) {
			event.setCancelled(true);
			String userMessage = chatMessage.substring(clientPrefix.length());
			String userCommand = userMessage;

			String[] args;

			if (userCommand.contains(" ")) {
				userCommand = userCommand.split(" ")[0];
				args = getArguments(userCommand, userMessage);
			} else {
				args = new String[0];
			}


			if (args.length == 1) {
				if (args[0].startsWith(" "))
					args = new String[0];
			}

			if (commandList.containsKey(userCommand)) {
				for (Entry<String, AClientCommand> clientCommand : commandList
						.entrySet()) {
					if (clientCommand.getKey().equalsIgnoreCase(userCommand)) {
						clientCommand.getValue().runCommand(args);
					}
				}
			} else if (Wrapper.getModuleManager().doesModuleExist(userCommand)) {
				Module module = Wrapper.getModuleManager().getModuleIgnoreCase(
						userCommand);

				if (module.isCommand()) {
					module.onCommand(args);
				} else {
					Chat.printChatMessage(module.getName() + " does not have a command!");
				}
			} else {
				Chat.printClientMessage("Unknown command! Type \""
						+ ClientSettings.getValue("client.prefix", String.class)
						+ "help\" for a list of commands.");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDisplayGuiScreen(DisplayGuiScreenEvent event) {
		if (event.getGuiScreen() instanceof GuiChat && !(event.getGuiScreen() instanceof GuiSleepMP)) {
			GuiChat guiChat = (GuiChat) event.getGuiScreen();
			GuiChatConsole guiChatConsole = new GuiChatConsole();
			guiChatConsole.setDefaultInputFieldText(guiChat.getDefaultInputFieldText());

			event.setGuiScreen(guiChatConsole);
		}
	}

	private String[] getArguments(String userCommand, String userMessage) {
		String argsWithoutWhiteSpace = userMessage.replaceFirst(userCommand, "");

		while (argsWithoutWhiteSpace.startsWith(" ")) {
			argsWithoutWhiteSpace = argsWithoutWhiteSpace.substring(1);
		}

		return argsWithoutWhiteSpace.split(" ");
	}

	@EventHandler
	public void onShutdown(StopGameEvent event) {
		Wrapper.getEventManager().unregister(this);
	}
}

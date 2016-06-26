package de.paxii.clarinet.gui.menu.chat;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.settings.ClientSettings;
import net.minecraft.client.gui.GuiChat;

import java.util.Map;

/**
 * Created by Lars on 21.02.2016.
 */
public class GuiChatConsole extends GuiChat {

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		String clientPrefix = ClientSettings.getValue("client.prefix", String.class);
		if (this.inputField.getText().startsWith(clientPrefix)) {
			String commandName = this.inputField.getText().substring(clientPrefix.length()).toLowerCase();
			String prediction = "help";

			if (commandName.length() > 0) {
				if (commandName.contains(" ") && !commandName.startsWith(" ")) {
					String[] commandSplit = commandName.split(" ");
					commandName = commandSplit[0].toLowerCase();
				}

				for (Map.Entry<String, AClientCommand> entry : Wrapper.getConsole().getCommandList().entrySet()) {
					if (entry.getKey().toLowerCase().startsWith(commandName)) {
						prediction = entry.getKey().toLowerCase();

						break;
					}
				}

				if (prediction.equals("help")) {
					for (Map.Entry<String, Module> entry : Wrapper.getModuleManager().getModuleList().entrySet()) {
						if (entry.getValue().isCommand()) {
							if (entry.getKey().toLowerCase().startsWith(commandName)) {
								prediction = entry.getKey().toLowerCase();

								break;
							}
						}
					}
				}

				if (prediction.equals("help")) {
					prediction = "";
				}
			}

			Wrapper.getFontRenderer().drawString(clientPrefix + prediction, 4, this.height - 12, 0xAAAAAA);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}

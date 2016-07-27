package de.paxii.clarinet;

import de.paxii.clarinet.command.ClientConsole;
import de.paxii.clarinet.event.EventManager;
import de.paxii.clarinet.gui.ingame.ClientClickableGui;
import de.paxii.clarinet.gui.ingame.settings.PanelSettingsHandler;
import de.paxii.clarinet.gui.menu.hooks.GuiMainMenuHook;
import de.paxii.clarinet.gui.menu.login.GuiAltManager;
import de.paxii.clarinet.module.ModuleManager;
import de.paxii.clarinet.util.chat.font.FontManager;
import de.paxii.clarinet.util.encryption.StringEncryption;
import de.paxii.clarinet.util.module.file.FileManager;
import de.paxii.clarinet.util.module.friends.FriendManager;
import de.paxii.clarinet.util.module.friends.FriendSettingsHandler;
import de.paxii.clarinet.util.module.settings.ModuleSettingsHandler;
import de.paxii.clarinet.util.module.store.ModuleStore;
import de.paxii.clarinet.util.notifications.NotificationManager;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.settings.ClientSettingsHandler;
import de.paxii.clarinet.util.update.UpdateChecker;
import lombok.Getter;

import java.io.File;

@Getter
public class Client {
	@Getter
	private static Client clientInstance;
	@Getter
	private static final String clientName = "Matix";
	@Getter
	private static final String clientVersion = "1.6.1B";
	@Getter
	private static final String gameVersion = "1.10";
	@Getter
	private static final int clientBuild = 16005;
	@Getter
	private static final String clientURL = "http://paxii.de/Matix/";

	private EventManager eventManager;
	private ModuleManager moduleManager;
	private FileManager fileManager;
	private FriendManager friendManager;
	private NotificationManager notificationManager;
	private FontManager fontManager;

	private ModuleStore moduleStore;

	private ClientConsole clientConsole;
	private ClientClickableGui clientClickableGui;

	private ClientSettingsHandler clientSettingsHandler;
	private ModuleSettingsHandler moduleSettingsHandler;
	private PanelSettingsHandler panelSettingsHandler;
	private FriendSettingsHandler friendSettingsHandler;

	private GuiAltManager altManger;

	private StringEncryption stringEncryption;

	private UpdateChecker updateChecker;

	public Client() {
		Client.clientInstance = this;

		File clientFolder = new File(ClientSettings.getClientFolderPath().getValue());

		if (!clientFolder.exists()) {
			clientFolder.mkdirs();
		}

		this.eventManager = new EventManager();

		this.clientSettingsHandler = new ClientSettingsHandler();
		this.stringEncryption = new StringEncryption();

		this.moduleStore = new ModuleStore();

		this.moduleManager = new ModuleManager();
		this.fileManager = new FileManager();
		this.friendManager = new FriendManager();
		this.notificationManager = new NotificationManager();
		this.fontManager = new FontManager();

		this.clientConsole = new ClientConsole();
		this.clientClickableGui = new ClientClickableGui();

		this.moduleSettingsHandler = new ModuleSettingsHandler();
		this.panelSettingsHandler = new PanelSettingsHandler();
		this.friendSettingsHandler = new FriendSettingsHandler();

		this.altManger = new GuiAltManager(new GuiMainMenuHook());

		this.updateChecker = new UpdateChecker();
	}
}

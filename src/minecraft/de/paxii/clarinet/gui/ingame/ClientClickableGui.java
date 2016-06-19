package de.paxii.clarinet.gui.ingame;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.client.PostLoadModulesEvent;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.GuiPanelManager;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.gui.ingame.panel.theme.IClientTheme;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.DefaultClientTheme;
import de.paxii.clarinet.util.settings.ClientSetting;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.threads.ConcurrentArrayList;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientClickableGui extends GuiScreen {
	@Getter
	private IClientTheme currentTheme;
	@Getter
	private ConcurrentArrayList<GuiPanel> guiPanels;
	@Getter
	private ConcurrentArrayList<IClientTheme> panelThemes;

	private GuiPanelManager panelManager;

	public ClientClickableGui() {
		this.guiPanels = new ConcurrentArrayList<>();
		this.panelThemes = new ConcurrentArrayList<>();
		this.panelManager = new GuiPanelManager();

		Wrapper.getEventManager().register(this);

		this.loadThemes();
	}

	@EventHandler
	private void onModulesLoad(PostLoadModulesEvent event) {
		this.loadPanels();
	}

	public void loadThemes() {
		this.panelThemes.add(new DefaultClientTheme());
		this.setCurrentTheme(this.panelThemes.get(0));
	}

	public void setCurrentTheme(IClientTheme clientTheme) {
		//TODO: Implement proper color changing?
		GuiPanel colorPanel = this.getGuiPanel("Gui Color");

		if (colorPanel != null) {
			if (clientTheme instanceof DefaultClientTheme) {
				colorPanel.setVisible(true);
			} else {
				colorPanel.setVisible(false);
			}
		}

		this.currentTheme = clientTheme;
		ClientSettings
				.getClientSettings()
				.put("client.guitheme",
						(new ClientSetting("client.guitheme", this
								.getCurrentTheme().getName())));
	}

	public void loadPanels() {
		new Thread(() -> {
			System.out.println("loading Panels...");
			reloadPanels();

			AtomicBoolean error = new AtomicBoolean();
			this.getGuiPanels().forEach((guiPanel -> {
				if (guiPanel.isVisible() && guiPanel.getPanelElements().size() == 0) {
					error.set(true);
				}
			}));

			if (error.get()) {
				this.reloadPanels();
			}
		}).start();
	}

	private void reloadPanels() {
		this.panelManager.loadPanels(this);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.getGuiPanels().forEach(guiPanel -> {
			if (guiPanel.isVisible()) {
				guiPanel.drawPanel(mouseX, mouseY);
			}
		});

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int buttonClicked)
			throws IOException {
		for (int i = 0; i < this.getGuiPanels().size(); i++) {
			GuiPanel guiPanel = this.getGuiPanels().get(i);

			if (!guiPanel.isVisible()) {
				continue;
			}

			if (guiPanel.isMouseOverAll(mouseX, mouseY)) {
				if (this.getGuiPanels().indexOf(guiPanel) == this
						.getGuiPanels().size() - 1) {
					guiPanel.mouseClicked(mouseX, mouseY, buttonClicked);
				}

				this.getGuiPanels().remove(guiPanel);
				this.getGuiPanels().add(guiPanel);
			}
		}

		super.mouseClicked(mouseX, mouseY, buttonClicked);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int buttonReleased) {
		if (buttonReleased == 0) {
			for (GuiPanel guiPanel : this.getGuiPanels()) {
				guiPanel.setDragging(false);

				for (PanelElement panelElement : guiPanel.getPanelElements()) {
					panelElement.mouseMovedOrUp(mouseX, mouseY, buttonReleased);
				}
			}
		}

		super.mouseReleased(mouseX, mouseY, buttonReleased);
	}

	public boolean doesPanelExist(String panelName) {
		for (GuiPanel guiPanel : this.getGuiPanels()) {
			if (guiPanel.getPanelName().equals(panelName))
				return true;
		}

		return false;
	}

	public GuiPanel getGuiPanel(String panelName) {
		for (GuiPanel guiPanel : this.getGuiPanels()) {
			if (guiPanel.getPanelName().equals(panelName))
				return guiPanel;
		}

		return null;
	}

	public boolean doesThemeExist(String themeName) {
		for (IClientTheme theme : this.getPanelThemes()) {
			if (theme.getName().equalsIgnoreCase(themeName))
				return true;
		}

		return false;
	}

	public IClientTheme getTheme(String themeName) {
		for (IClientTheme theme : this.getPanelThemes()) {
			if (theme.getName().equalsIgnoreCase(themeName))
				return theme;
		}

		return null;
	}
}

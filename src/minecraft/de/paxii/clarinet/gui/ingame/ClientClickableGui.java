package de.paxii.clarinet.gui.ingame;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.client.PostLoadModulesEvent;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.GuiPanelManager;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.gui.ingame.panel.theme.GuiTheme;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.DefaultTheme;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.LegacyTheme;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.Matix2HDTheme;
import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.settings.type.ClientSettingString;

import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import net.minecraft.util.EnumActionResult;

public class ClientClickableGui extends GuiScreen {
  private final GuiPanelManager panelManager;
  @Getter
  private GuiTheme currentTheme;
  @Getter
  private ArrayList<GuiPanel> guiPanels;
  @Getter
  private ArrayList<GuiTheme> panelThemes;

  public ClientClickableGui() {
    this.guiPanels = new ArrayList<>();
    this.panelThemes = new ArrayList<>();
    this.panelManager = new GuiPanelManager();

    Wrapper.getEventManager().register(this);
  }

  @EventHandler
  private void onModulesLoad(PostLoadModulesEvent event) {
    this.loadThemes();
    this.loadPanels();
  }

  private void loadThemes() {
    this.panelThemes.add(new DefaultTheme());
    this.panelThemes.add(new Matix2HDTheme());
    this.panelThemes.add(new LegacyTheme());

    GuiTheme clientTheme = this.getTheme(ClientSettings.getValue("client.guitheme", String.class));

    if (clientTheme != null) {
      this.setCurrentTheme(clientTheme);
    } else {
      this.setCurrentTheme(this.panelThemes.get(0));
    }
  }

  public void setCurrentTheme(GuiTheme clientTheme) {
    //TODO: Implement proper color changing?
    GuiPanel colorPanel = this.getGuiPanel("Gui Color");

    if (colorPanel != null) {
      if (clientTheme instanceof LegacyTheme) {
        colorPanel.setVisible(true);
      } else {
        colorPanel.setVisible(false);
      }
    }

    this.currentTheme = clientTheme;
    ClientSettings.put(new ClientSettingString("client.guitheme", this.getCurrentTheme().getName()));
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

        this.moveOverAll(guiPanel);
      }
    }

    super.mouseClicked(mouseX, mouseY, buttonClicked);
  }

  public void moveOverAll(GuiPanel guiPanel) {
    this.getGuiPanels().remove(guiPanel);
    this.getGuiPanels().add(guiPanel);
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

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    EnumActionResult result = EnumActionResult.PASS;
    for (GuiPanel guiPanel : this.getGuiPanels()) {
      EnumActionResult actionResult = guiPanel.keyPressed(keyCode);
      if (result == EnumActionResult.PASS) {
        result = actionResult;
      }
    }

    if (result == EnumActionResult.PASS) {
      super.keyTyped(typedChar, keyCode);
    }
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
    for (GuiTheme theme : this.getPanelThemes()) {
      if (theme.getName().equalsIgnoreCase(themeName))
        return true;
    }

    return false;
  }

  public GuiTheme getTheme(String themeName) {
    for (GuiTheme theme : this.getPanelThemes()) {
      if (theme.getName().equalsIgnoreCase(themeName))
        return theme;
    }

    return null;
  }
}

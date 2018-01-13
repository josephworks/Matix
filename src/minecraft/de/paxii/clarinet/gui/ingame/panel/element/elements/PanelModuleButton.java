package de.paxii.clarinet.gui.ingame.panel.element.elements;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.events.player.PlayerSendChatMessageEvent;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.gui.ingame.panel.GuiPanelModuleSettings;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.gui.ingame.panel.theme.layout.ElementSpacing;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import de.paxii.clarinet.util.settings.ClientSetting;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.util.function.Function;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;

import lombok.Getter;
import org.lwjgl.input.Keyboard;

public class PanelModuleButton extends PanelElement {
  private final GuiPanel guiPanel;
  @Getter
  private final Module module;
  @Getter
  private GuiPanelModuleSettings moduleSettings;
  private TimeManager timeManager;
  private boolean wasHovered;

  public PanelModuleButton(Module module, GuiPanel guiPanel) {
    super(90, 12);

    this.guiPanel = guiPanel;
    this.module = module;
    this.timeManager = new TimeManager();

    this.moduleSettings = new GuiPanelModuleSettings(this.module, 0, 0) {
      @Override
      public void addElements() {
        Module module = PanelModuleButton.this.module;
        ArrayList<ClientSetting> settings = new ArrayList<>(module.getModuleSettings().values());
        settings.sort(null);
        settings.forEach(setting -> {
          if (setting.getValue().getClass().getName().equals("java.lang.Boolean")) {
            try {
              this.getPanelElements().add(
                      new PanelCheckBox(setting.getName(), (boolean) setting.getValue()) {
                        @Override
                        @SuppressWarnings(value = "unchecked")
                        public void onUpdate(Boolean newValue, Boolean oldValue) {
                          setting.setValue(newValue);
                        }
                      }
              );
            } catch (ClassCastException exception) {
              exception.printStackTrace();
            }
          }
        });
        this.getPanelElements().addAll(module.getGuiPanelElements());
        module.getModuleValues().forEach((valueName, value) -> {
          PanelSlider panelSlider = new PanelSlider(value, value.isShouldRound());
          this.getPanelElements().add(panelSlider);
        });
        Function<Integer, String> caption = keyCode -> "Keybind: " + (keyCode != -1 ? Keyboard.getKeyName(keyCode) : "None");
        this.getPanelElements().add(new PanelKeyBindButton(caption.apply(module.getKey()), (keyCode, self) -> {
          if (keyCode == 1) {
            keyCode = -1;
          }
          module.setKey(keyCode);
          self.setCaption(caption.apply(keyCode));

          return false;
        }));
      }
    };

    Wrapper.getClickableGui().getGuiPanels().add(this.moduleSettings);
  }

  @Override
  public void drawElement(int elementX, int elementY, int mouseX, int mouseY) {
    boolean buttonHovered = this.isMouseOverButton(mouseX, mouseY);
    boolean hasSettings = this.moduleSettings.getPanelElements().size() > 0;
    boolean displayHelp = false;

    this.timeManager.updateTimer();

    if (buttonHovered) {
      if (!this.wasHovered) {
        this.timeManager.updateLast();
        this.wasHovered = true;
      }
    } else {
      this.wasHovered = false;
    }

    if (this.timeManager.sleep(1000L) && this.wasHovered) {
      displayHelp = true;
    }

    this.moduleSettings.setPanelX(elementX + this.getWidth() + this.getElementSpacing().getMarginRight());
    this.moduleSettings.setPanelY(elementY);

    Wrapper.getClickableGui()
            .getCurrentTheme()
            .drawModuleButton(module, elementX, elementY, this.getWidth(),
                    this.getHeight(), buttonHovered, hasSettings, displayHelp);

    super.drawElement(elementX, elementY, mouseX, mouseY);
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
    if (this.isMouseOverButton(mouseX, mouseY)) {
      if (clickedButton == 0) {
        this.module.toggle();
      } else if (clickedButton == 1) {
        this.guiPanel.getPanelElements().stream()
                .filter(panelElement -> panelElement instanceof PanelModuleButton && panelElement != this)
                .forEach(panelElement -> ((PanelModuleButton) panelElement).getModuleSettings().setVisible(false));
        this.moduleSettings.setOpened(true);

        if (this.getModuleSettings().getPanelElements().size() > 0) {
          Wrapper.getClickableGui().moveOverAll(this.moduleSettings);
          this.moduleSettings.setVisible(!this.moduleSettings.isVisible());
        } else {
          this.moduleSettings.setVisible(false);
        }
      } else if (clickedButton == 2) {
        Wrapper.getConsole().onChatMessage(new PlayerSendChatMessageEvent(new CPacketChatMessage(
                ClientSettings.getValue("client.prefix", String.class) + "help " + this.getModule().getName().toLowerCase()
        )));
      }
    }
  }

  @Override
  public ElementSpacing getElementSpacing() {
    return Wrapper.getClickableGui().getCurrentTheme().getLayout().getModuleButtonLayout();
  }
}

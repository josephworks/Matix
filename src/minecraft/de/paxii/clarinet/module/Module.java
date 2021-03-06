package de.paxii.clarinet.module;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.events.Event;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.chat.ChatColor;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.settings.ClientSetting;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class Module implements Comparable<Module> {
  private final String name;
  private ModuleCategory category;
  private int key;
  private String syntax = "", description = "";
  private String helpUrl;
  private boolean command;
  private boolean enabled;
  @Setter(value = AccessLevel.PROTECTED)
  private boolean registered;
  private boolean displayedInGui;
  private boolean plugin;
  @Setter(value = AccessLevel.PROTECTED)
  private int buildVersion;
  @Setter(value = AccessLevel.PROTECTED)
  private String version;
  private HashMap<String, ValueBase> moduleValues;
  private HashMap<String, ClientSetting> moduleSettings;
  private ArrayList<PanelElement> guiPanelElements;

  public Module(String name, ModuleCategory category) {
    this.name = name;
    this.category = category;
    this.setKey(-1);
    this.setHelpUrl(Client.getClientURL() + "docs/modules/" + this.category.toString().toLowerCase() + "/" + this.getName().toLowerCase() + ".html");
    this.setBuildVersion(Client.getClientBuild());
    this.setVersion(Client.getClientVersion());

    this.moduleValues = new HashMap<>();
    this.moduleSettings = new HashMap<>();
    this.guiPanelElements = new ArrayList<>();
    this.setDisplayedInGui(true);
  }

  public Module(String name, ModuleCategory category, int i) {
    this(name, category);

    this.setKey(i);
  }

  public final void setEnabled(boolean enabled) {
    if (this.enabled == enabled)
      return;

    this.enabled = enabled;

    if (isEnabled()) {
      if (this.isRegistered()) {
        this.register();
      }
      this.onEnable();
    } else {
      this.onDisable();
      if (this.isRegistered()) {
        this.unregister();
      }
    }

    this.onToggle();
  }

  public final void setKey(int key) {
    this.key = key;

    if (key != -1) {
      Wrapper.getModuleManager().addKey(this.name, key);
    } else {
      Wrapper.getModuleManager().removeKey(this.name);
    }
  }

  public final void toggle() {
    setEnabled(!isEnabled());
  }

  public void onEnable() {
  }

  public void onDisable() {
  }

  public void onToggle() {
  }

  public void onStartup() {
  }

  public void onShutdown() {
  }

  public void onCommand(String[] args) {
  }

  public String toString() {
    return this.getName();
  }

  public <T extends ClientSetting> T getSetting(String settingName, Class<T> type) {
    return type.cast(this.moduleSettings.get(settingName));
  }

  public <T> T setValue(String settingName, T value) {
    if (!this.getModuleSettings().containsKey(settingName)) {
      this.getModuleSettings().put(settingName, new ClientSetting<>(settingName, value));
    }

    ClientSetting<T> clientSetting = this.getModuleSettings().get(settingName);
    clientSetting.setValue(value);

    return value;
  }

  public <T> T getValue(String settingName, Class<T> type) {
    return type.cast(this.moduleSettings.get(settingName).getValue());
  }

  public <T> T getValueOrDefault(String settingName, Class<T> type, T defaultValue) {
    if (this.moduleSettings.containsKey(settingName)) {
      return type.cast(this.moduleSettings.get(settingName).getValue());
    }

    return defaultValue;
  }

  public ValueBase getValueBase(String valueName) {
    return this.getModuleValues().get(valueName);
  }

  public void addPanelElement(PanelElement panelElement) {
    this.guiPanelElements.add(panelElement);
  }

  public void removePanelElement(PanelElement panelElement) {
    this.guiPanelElements.remove(panelElement);
  }

  protected void sendClientMessage(String message) {
    Chat.printClientMessage(ChatColor.AQUA + "[" + this.getName() + "] " + ChatColor.RESET + message);
  }

  protected void register() {
    Wrapper.getEventManager().register(this);
  }

  protected void register(Class<? extends Event> eventClass) {
    Wrapper.getEventManager().register(this, eventClass);
  }

  protected void unregister() {
    Wrapper.getEventManager().unregister(this);
  }

  protected void unregister(Class<? extends Event> eventClass) {
    Wrapper.getEventManager().unregister(this, eventClass);
  }

  @Override
  public int compareTo(Module o) {
    return this.getName().compareToIgnoreCase(o.getName());
  }
}

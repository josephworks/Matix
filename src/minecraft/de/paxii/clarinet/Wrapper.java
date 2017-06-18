package de.paxii.clarinet;

import de.paxii.clarinet.command.ClientConsole;
import de.paxii.clarinet.event.EventManager;
import de.paxii.clarinet.gui.ingame.ClientClickableGui;
import de.paxii.clarinet.module.ModuleManager;
import de.paxii.clarinet.util.encryption.StringEncryption;
import de.paxii.clarinet.util.module.friends.FriendManager;
import de.paxii.clarinet.util.settings.ClientSetting;
import de.paxii.clarinet.util.settings.ClientSettings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Timer;

import java.util.HashMap;

public class Wrapper {
  public static Client getClient() {
    return Client.getClientInstance();
  }

  public static ClientConsole getConsole() {
    return getClient().getClientConsole();
  }

  public static ClientClickableGui getClickableGui() {
    return getClient().getClientClickableGui();
  }

  public static HashMap<String, ClientSetting> getSettings() {
    return ClientSettings.getClientSettings();
  }

  public static EventManager getEventManager() {
    return getClient().getEventManager();
  }

  public static ModuleManager getModuleManager() {
    return getClient().getModuleManager();
  }

  public static FriendManager getFriendManager() {
    return getClient().getFriendManager();
  }

  public static StringEncryption getStringEncryption() {
    return getClient().getStringEncryption();
  }

  public static Minecraft getMinecraft() {
    return Minecraft.getMinecraft();
  }

  public static EntityPlayerSP getPlayer() {
    return getMinecraft().player;
  }

  public static WorldClient getWorld() {
    return getMinecraft().world;
  }

  public static FontRenderer getFontRenderer() {
    return getMinecraft().fontRendererObj;
  }

  public static EntityRenderer getRenderer() {
    return getMinecraft().entityRenderer;
  }

  public static GameSettings getGameSettings() {
    return getMinecraft().gameSettings;
  }

  public static Timer getTimer() {
    return getMinecraft().getTimer();
  }

  public static NetHandlerPlayClient getSendQueue() {
    return getPlayer().connection;
  }

  public static ScaledResolution getScaledResolution() {
    return new ScaledResolution(getMinecraft());
  }
}

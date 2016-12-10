package de.paxii.clarinet.util.login.mcleaks;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.gui.DisplayGuiScreenEvent;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.NetworkManager;

import java.lang.reflect.Field;

import io.netty.channel.Channel;

/**
 * Created by Lars on 17.09.2016.
 */
public class MCLeaksListener {

  @EventHandler
  public void onDisplayGui(DisplayGuiScreenEvent screenEvent) {
    if (Wrapper.getClient().getMcLeaksManager().isUsingMcLeaks() && screenEvent.getGuiScreen() instanceof GuiConnecting) {
      new Thread(() -> {
        try {
          Field networkManagerField = GuiConnecting.class.getDeclaredField("networkManager");
          Field channelField = NetworkManager.class.getDeclaredField("channel");
          channelField.setAccessible(true);
          networkManagerField.setAccessible(true);
          NetworkManager networkManager = null;

          while (networkManager == null) {
            try {
              Thread.sleep(2L);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            networkManager = (NetworkManager) networkManagerField.get(screenEvent.getGuiScreen());
          }

          Wrapper.getClient().getMcLeaksManager().setNetworkManager(networkManager);
          Channel channel = (Channel) channelField.get(networkManager);
          channel.pipeline().addBefore("packet_handler", "mcleaks_handler", new MCLeaksChannelHandler());
        } catch (ReflectiveOperationException e) {
          e.printStackTrace();
        }
      }).start();
    }
  }
}

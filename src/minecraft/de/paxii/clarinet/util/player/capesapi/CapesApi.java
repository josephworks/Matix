package de.paxii.clarinet.util.player.capesapi;

import com.mojang.authlib.GameProfile;

import de.paxii.clarinet.Wrapper;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.src.CapeUtils;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Lars on 09.03.2017.
 */
public class CapesApi {

  private static final String BASE_URL = "http://capesapi.com/api/v1/%s/getCape";

  private static final ArrayList<GameProfile> pendingRequests = new ArrayList<>();
  private static final HashMap<String, ResourceLocation> capeMap = new HashMap<>();

  public static void loadCape(final GameProfile gameProfile) {
    if (CapesApi.hasPendingRequests(gameProfile)) {
      return;
    }

    CapesApi.addCape(gameProfile, null);
    String url = String.format(CapesApi.BASE_URL, gameProfile.getId().toString());
    ResourceLocation resourceLocation = new ResourceLocation(
            String.format("capesapi/capes/%s.png", new Date().getTime())
    );
    TextureManager textureManager = Wrapper.getMinecraft().getTextureManager();

    ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {
      @Override
      public BufferedImage parseUserSkin(BufferedImage image) {
        return CapeUtils.parseCape(image);
      }

      @Override
      public void skinAvailable() {
        CapesApi.addCape(gameProfile, resourceLocation);
        CapesApi.pendingRequests.remove(gameProfile);
      }
    });
    textureManager.loadTexture(resourceLocation, threadDownloadImageData);
    CapesApi.pendingRequests.add(gameProfile);
  }

  public static void addCape(GameProfile gameProfile, ResourceLocation resourceLocation) {
    CapesApi.capeMap.put(gameProfile.getId().toString(), resourceLocation);
  }

  public static ResourceLocation getCape(GameProfile gameProfile) {
    return CapesApi.capeMap.getOrDefault(gameProfile.getId().toString(), null);
  }

  public static boolean hasCape(GameProfile gameProfile) {
    boolean hasCape = CapesApi.capeMap.containsKey(gameProfile.getId().toString());
    ResourceLocation resourceLocation = CapesApi.capeMap.get(gameProfile.getId().toString());

    if (hasCape && resourceLocation == null && !CapesApi.hasPendingRequests(gameProfile)) {
      CapesApi.loadCape(gameProfile);
      return false;
    }

    return hasCape && resourceLocation != null;
  }

  public static void reset() {
    CapesApi.capeMap.keySet().forEach((userId) -> CapesApi.capeMap.put(userId, null));
    CapesApi.pendingRequests.clear();
  }

  private static boolean hasPendingRequests(GameProfile gameProfile) {
    return CapesApi.pendingRequests.contains(gameProfile);
  }

}

package de.paxii.clarinet.util.protocol;

import lombok.Getter;

/**
 * Created by Lars on 11.01.2017.
 */

public class ProtocolVersion {
  private static CompatibleVersion currentVersion = CompatibleVersion._1122;

  public static int getProtocolVersion() {
    return currentVersion.getProtocolVersion();
  }

  public static String getGameVersion() {
    return currentVersion.getGameVersion();
  }

  public static void cycleVersion() {
    int currentIndex = currentVersion.ordinal();
    if (currentIndex < CompatibleVersion.values().length - 1) {
      currentIndex++;
    } else {
      currentIndex = 0;
    }

    currentVersion = CompatibleVersion.values()[currentIndex];
  }

  public enum CompatibleVersion {
    _1122(340, "1.12.2");

    @Getter
    private int protocolVersion;
    @Getter
    private String gameVersion;

    CompatibleVersion(int protocolVersion, String gameVersion) {
      this.protocolVersion = protocolVersion;
      this.gameVersion = gameVersion;
    }
  }
}

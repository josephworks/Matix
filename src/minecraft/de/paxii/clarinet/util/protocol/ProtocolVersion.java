package de.paxii.clarinet.util.protocol;

import lombok.Getter;

/**
 * Created by Lars on 11.01.2017.
 */

public class ProtocolVersion {
  private static CompatibleVersion currentVersion = CompatibleVersion._1112;

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
    _1112(316, "1.11.2"),
    _1110(315, "1.11");

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

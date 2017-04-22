package de.paxii.clarinet.util.update;

import lombok.Getter;

public class VersionObject {
  @Getter
  private int clientBuild;
  @Getter
  private String gameVersion;
  @Getter
  private String url;

  public VersionObject(int clientBuild, String gameVersion, String url) {
    this.clientBuild = clientBuild;
    this.gameVersion = gameVersion;
    this.url = url;
  }
}

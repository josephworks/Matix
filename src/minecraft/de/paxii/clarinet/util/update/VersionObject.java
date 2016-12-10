package de.paxii.clarinet.util.update;

import lombok.Getter;

public class VersionObject {
  @Getter
  private int clientBuild;
  @Getter
  private String gameVersion;

  public VersionObject(int clientBuild, String gameVersion) {
    this.clientBuild = clientBuild;
    this.gameVersion = gameVersion;
  }
}

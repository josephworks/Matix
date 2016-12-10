package de.paxii.clarinet.util.json;

import lombok.Getter;

public class StringObject {
  @Getter
  private String string;

  public StringObject(String string) {
    this.string = string;
  }
}

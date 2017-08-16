package de.paxii.clarinet.gui.menu.login;

import lombok.Data;

@Data
public class AltObject implements Comparable<AltObject> {
  private String userName;
  private String email;
  private String password;

  public AltObject(String userName, String email, String password) {
    this.userName = userName;
    this.email = email;
    this.password = password;
  }

  public AltObject(String userName, String password) {
    this(userName, "", password);
  }

  public AltObject(String userName) {
    this(userName, "");
  }

  public boolean isPremium() {
    return this.password.length() > 0;
  }

  public boolean isMojang() {
    return this.email.length() > 0;
  }

  @Override
  public int compareTo(AltObject o) {
    return this.getUserName().compareToIgnoreCase(o.getUserName());
  }

  public AltObject copy() {
    return new AltObject(this.userName, this.email, this.password);
  }
}

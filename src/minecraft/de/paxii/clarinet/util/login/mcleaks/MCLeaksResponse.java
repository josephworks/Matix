package de.paxii.clarinet.util.login.mcleaks;

import java.util.HashMap;

import lombok.Data;

/**
 * Created by Lars on 04.09.2016.
 */
@Data
public class MCLeaksResponse {
  private boolean success;
  private String errorMessage;
  private HashMap<String, String> result;

  public MCLeaksResponse() {
    this.result = new HashMap<>();
  }

  public String getUsername() {
    return this.result.get("mcname");
  }

  public String getSession() {
    return this.result.get("session");
  }
}

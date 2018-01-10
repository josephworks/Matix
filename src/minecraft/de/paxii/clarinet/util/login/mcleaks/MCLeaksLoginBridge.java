package de.paxii.clarinet.util.login.mcleaks;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.web.JsonFetcher;

import net.minecraft.util.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lars on 04.09.2016.
 */
public class MCLeaksLoginBridge {
  private static final String endPoint = "https://auth.mcleaks.net/v1/%s";

  public static Session loginWithToken(String token) {
    MCLeaksResponse mcLeaksResponse = JsonFetcher.post(String.format(endPoint, "redeem"), String.format("{\"token\": \"%s\"}", token), MCLeaksResponse.class);

    if (mcLeaksResponse != null) {
      if (mcLeaksResponse.isSuccess()) {
        Session session = new Session(mcLeaksResponse.getUsername(), "", "", "mojang");

        Wrapper.getMinecraft().setSession(session);
        Wrapper.getClient().getMcLeaksManager().setMcLeaksName(mcLeaksResponse.getUsername());
        Wrapper.getClient().getMcLeaksManager().setMcLeaksSession(mcLeaksResponse.getSession());
        Wrapper.getClient().getMcLeaksManager().setUsingMcLeaks(true);

        return session;
      }
    }

    return null;
  }

  public static Session loginWithNewToken() {
    HashMap<String, String> requestProperties = new HashMap<>();
    requestProperties.put("Content-Type", "application/x-www-form-urlencoded");
    JsonFetcher.post("https://mcleaks.net/get", "posttype=true", String.class, requestProperties);
    String response = JsonFetcher.get("https://mcleaks.net/get", String.class);
    Pattern pattern = Pattern.compile("<input type=\"text\" class=\"form-control\" value=\"(.*?)\">");
    Matcher matcher = pattern.matcher(response);
    ArrayList<String> inputFields = new ArrayList<>();

    while (matcher.find()) {
      inputFields.add(matcher.group(1));
    }

    if (inputFields.size() >= 2) {
      return MCLeaksLoginBridge.loginWithToken(inputFields.get(1));
    }

    return null;
  }

  public static void joinServer(String session, String userName, String server, String serverHash, Runnable callBack) {
    HashMap<String, String> requestData = new HashMap<>();
    HashMap<String, String> requestProperties = new HashMap<>();
    requestData.put("session", session);
    requestData.put("mcname", userName);
    requestData.put("server", server);
    requestData.put("serverhash", serverHash);
    requestProperties.put("Content-Type", "application/x-www-form-urlencoded");

    MCLeaksResponse mcLeaksResponse = JsonFetcher.post(String.format(endPoint, "joinserver"), requestData, MCLeaksResponse.class, requestProperties);

    if (mcLeaksResponse.isSuccess()) {
      callBack.run();
    }
  }
}

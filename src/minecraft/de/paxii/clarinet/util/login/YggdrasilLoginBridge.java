package de.paxii.clarinet.util.login;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.menu.login.AltObject;
import net.minecraft.util.Session;

import java.net.Proxy;
import java.util.UUID;

public class YggdrasilLoginBridge {
	public static Session loginWithPassword(String username, String password) {
		UserAuthentication auth = new YggdrasilUserAuthentication(
				new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID
						.randomUUID().toString()), Agent.MINECRAFT);
		auth.setUsername(username);
		auth.setPassword(password);

		try {
			auth.logIn();
			String userName = auth.getSelectedProfile().getName();
			UUID playerUUID = auth.getSelectedProfile().getId();
			String accessToken = auth.getAuthenticatedToken();

			System.out.println(userName + "'s (UUID: '" + playerUUID.toString() + "') accessToken: " + accessToken);

			Session session = new Session(userName, playerUUID.toString(), accessToken,
					username.contains("@") ? "mojang" : "legacy");

			Wrapper.getMinecraft().setSession(session);

			return session;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Session loginWithoutPassword(String username) {
		Wrapper.getMinecraft().setSession(new Session(username, "", "", "legacy"));

		return Wrapper.getMinecraft().getSession();
	}

	public static Session loginWithAlt(AltObject alt) {
		if (alt.isPremium()) {
			return YggdrasilLoginBridge.loginWithPassword(alt.isMojang() ? alt.getEmail() : alt.getUserName(), alt.getPassword());
		} else {
			return YggdrasilLoginBridge.loginWithoutPassword(alt.getUserName());
		}
	}
}

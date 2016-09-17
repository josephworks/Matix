package de.paxii.clarinet.util.login.mcleaks;

import de.paxii.clarinet.Wrapper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.NetworkManager;

/**
 * Created by Lars on 17.09.2016.
 */
public class MCLeaksManager {
	@Getter
	@Setter
	private boolean usingMcLeaks;
	@Getter
	@Setter
	private NetworkManager networkManager;
	@Getter
	@Setter
	private String mcLeaksName;
	@Getter
	@Setter
	private String mcLeaksSession;
	private MCLeaksListener mcLeaksListener;

	public MCLeaksManager() {
		Wrapper.getEventManager().register(this.mcLeaksListener = new MCLeaksListener());
	}
}

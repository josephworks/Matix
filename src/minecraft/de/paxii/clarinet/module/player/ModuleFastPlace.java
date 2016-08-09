package de.paxii.clarinet.module.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.module.settings.ValueBase;

public class ModuleFastPlace extends Module {
	public ModuleFastPlace() {
		super("Fastplace", ModuleCategory.PLAYER, -1);

		this.setRegistered(true);
		this.getModuleValues().put("clickSpeed",
				new ValueBase("FastPlace Delay", 0.0F, 0.0F, 4.0F, false, "Delay"));
	}

	@EventHandler
	public void onTick(IngameTickEvent event) {
		try {
			Wrapper.getMinecraft().setRightClickDelayTimer(Math.round(this.getModuleValues().get("clickSpeed").getValue()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getMinecraft().setRightClickDelayTimer(4);
	}
}

package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Map.Entry;

public class ModuleArraylist extends Module {
	public ModuleArraylist() {
		super("Arraylist", ModuleCategory.RENDER);

		this.setDescription("Renders a list of enabled modules on your screen.");

		this.setEnabled(true);
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onTick(IngameTickEvent event) {
		if (Wrapper.getMinecraft().gameSettings.showDebugInfo || Wrapper.getMinecraft().gameSettings.hideGUI) {
			return;
		}

		ScaledResolution scaledResolution = Wrapper.getScaledResolution();
		int indexY = 2;
		for (Entry<String, Module> moduleEntry : Wrapper.getModuleManager()
				.getModuleList().entrySet()) {
			Module module = moduleEntry.getValue();

			if (module.isEnabled() && module.isDisplayedInGui() && !module.getName().equals(this.getName())) {
				Wrapper.getFontRenderer().drawStringWithShadow(
						module.getName(),
						(scaledResolution.getScaledWidth()
								- Wrapper.getFontRenderer().getStringWidth(
								module.getName())) - 2, indexY,
						module.getCategory().getColor());

				indexY += 10;
			}
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

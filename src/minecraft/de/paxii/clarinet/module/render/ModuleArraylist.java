package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class ModuleArraylist extends Module {
	public ModuleArraylist() {
		super("Arraylist", ModuleCategory.RENDER);

		this.setRegistered(true);
		this.setDescription("Renders a list of enabled modules on your screen.");

		this.setEnabled(true);
	}

	@EventHandler
	public void onTick(IngameTickEvent event) {
		if (Wrapper.getMinecraft().gameSettings.showDebugInfo || Wrapper.getMinecraft().gameSettings.hideGUI) {
			return;
		}

		ScaledResolution scaledResolution = Wrapper.getScaledResolution();
		FontRenderer fontRenderer = Wrapper.getFontRenderer();
		int indexY = 2;

		Module[] sortedModules = Wrapper.getModuleManager().getModuleList().values().stream()
		                                .sorted((module, otherModule) -> Integer.compare(fontRenderer.getStringWidth(otherModule.getName()), fontRenderer.getStringWidth(module.getName())))
		                                .filter((module) -> module.isEnabled() && module.isDisplayedInGui() && module != this)
		                                .toArray(Module[]::new);

		for (Module module : sortedModules) {
			fontRenderer.drawStringWithShadow(
					module.getName(),
					(scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(module.getName())) - 2,
					indexY,
					module.getCategory().getColor()
			);

			indexY += 10;
		}
	}
}

package de.paxii.clarinet.module.render;

import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

/**
 * Created by Lars on 19.04.2016.
 */
public class ModuleHealthtags extends Module {
	public ModuleHealthtags() {
		super("Healthtags", ModuleCategory.RENDER);

		this.setDescription("Displays the current amount of health for players (max health is 20)");
	}
}

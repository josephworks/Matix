package de.paxii.clarinet.module.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PreMotionUpdateEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.util.Random;

public class ModuleSkinDerp extends Module {
	private Random random;

	public ModuleSkinDerp() {
		super("SkinDerp", ModuleCategory.PLAYER);

		this.setDescription("Rapidly enables and disables layers of your skin.");

		random = new Random();
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void preMotion(PreMotionUpdateEvent event) {
		for (int i = 0; i < 2; i++) {
			int maxSize = EnumPlayerModelParts.values().length - 1;
			EnumPlayerModelParts modelPart = EnumPlayerModelParts.values()[this.random.nextInt(maxSize) + 1];
			Wrapper.getGameSettings().setModelPartEnabled(modelPart, random.nextBoolean());
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
		for (EnumPlayerModelParts modelPart : EnumPlayerModelParts.values()) {
			Wrapper.getMinecraft().gameSettings.setModelPartEnabled(modelPart, true);
		}
	}
}

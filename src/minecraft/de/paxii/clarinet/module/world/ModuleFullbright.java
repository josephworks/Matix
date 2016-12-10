package de.paxii.clarinet.module.world;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;

import org.lwjgl.input.Keyboard;

public class ModuleFullbright extends Module {

  public ModuleFullbright() {
    super("Fullbright", ModuleCategory.WORLD, Keyboard.KEY_B);

    this.setRegistered(true);
    this.setDescription("Renders the world with full brightness.");
  }

  @EventHandler
  public void onTick(IngameTickEvent event) {
    if (Wrapper.getWorld().provider.getLightBrightnessTable()[0] != 1)
      for (int i = 0; 15 >= i; ++i)
        Wrapper.getWorld().provider.getLightBrightnessTable()[i] = 1;
  }


  @Override
  public void onDisable() {
    Wrapper.getWorld().provider.generateLightBrightnessTable();
  }
}

package de.paxii.clarinet.gui.menu.store.module;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.module.store.ModuleEntry;
import de.paxii.clarinet.util.module.store.ModuleStore;

import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

import java.util.Map;

/**
 * Created by Lars on 07.02.2016.
 */
public class GuiExternalModuleList extends GuiListExtended {
  private final GuiListExtended.IGuiListEntry[] keyEntries;

  public GuiExternalModuleList(GuiScreen parentScreen) {
    super(
            Wrapper.getMinecraft(),
            Wrapper.getScaledResolution().getScaledWidth(),
            Wrapper.getScaledResolution().getScaledHeight(),
            63,
            Wrapper.getScaledResolution().getScaledHeight() - 32,
            20
    );
    this.keyEntries = new GuiListExtended.IGuiListEntry[ModuleStore.listModules().size()];

    int index = 0;

    for (Map.Entry<String, ModuleEntry> moduleEntry : ModuleStore.listModules().entrySet()) {
      this.keyEntries[index] = new GuiListEntryExternalModule(moduleEntry.getValue(), parentScreen);
      index++;
    }

    this.slotHeight = 75;
  }

  @Override
  public IGuiListEntry getListEntry(int index) {
    return this.keyEntries[index];
  }

  @Override
  protected int getSize() {
    return this.keyEntries.length;
  }
}
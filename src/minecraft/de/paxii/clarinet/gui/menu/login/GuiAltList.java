package de.paxii.clarinet.gui.menu.login;

import de.paxii.clarinet.Wrapper;

import net.minecraft.client.gui.GuiListExtended;

import java.util.ArrayList;
import java.util.stream.Collectors;

import lombok.Getter;

public class GuiAltList extends GuiListExtended {

  @Getter
  private final ArrayList<GuiAltListEntry> entryList;

  public GuiAltList(GuiAltManager altManager, ArrayList<AltObject> alts) {
    super(
            Wrapper.getMinecraft(),
            Wrapper.getScaledResolution().getScaledWidth(),
            Wrapper.getScaledResolution().getScaledHeight(),
            63,
            Wrapper.getScaledResolution().getScaledHeight() - 55,
            20
    );

    this.entryList = alts.stream()
            .map(alt -> new GuiAltListEntry(alt, altManager))
            .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public IGuiListEntry getListEntry(int index) {
    return this.entryList.get(index);
  }

  @Override
  protected int getSize() {
    return this.entryList.size();
  }

  public ArrayList<AltObject> getAltObjects() {
    return this.entryList.stream()
            .map(GuiAltListEntry::getAlt)
            .collect(Collectors.toCollection(ArrayList::new));
  }
}

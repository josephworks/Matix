package de.paxii.clarinet.event.events.gui.sign;

import de.paxii.clarinet.event.events.Event;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;

import lombok.Data;

/**
 * Created by Lars on 03.02.2016.
 */
@Data
public class StopEditSignEvent implements Event {
  private ITextComponent[] signText;
  private int editLine;
  private boolean closeGui;

  public StopEditSignEvent(TileEntitySign tileEntitySign) {
    this.signText = tileEntitySign.signText;
  }
}

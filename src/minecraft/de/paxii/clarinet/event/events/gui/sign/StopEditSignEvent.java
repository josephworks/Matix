package de.paxii.clarinet.event.events.gui.sign;

import de.paxii.clarinet.event.events.Event;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;

import lombok.Data;

/**
 * Created by Lars on 03.02.2016.
 */
@Data
public class StopEditSignEvent implements Event {
  private IChatComponent[] signText;
  private int editLine;
  private boolean closeGui;

  public StopEditSignEvent(TileEntitySign tileEntitySign) {
    this.signText = tileEntitySign.signText;
  }
}

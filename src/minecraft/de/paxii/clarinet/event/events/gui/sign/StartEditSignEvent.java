package de.paxii.clarinet.event.events.gui.sign;

import de.paxii.clarinet.event.events.Event;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;

import lombok.Data;

/**
 * Created by Lars on 03.02.2016.
 */
@Data
public class StartEditSignEvent implements Event {
  private TileEntitySign tileEntitySign;
  private IChatComponent[] signText;
  private int editLine;
  private boolean closeGui;

  public StartEditSignEvent(TileEntitySign tileEntitySign) {
    this.tileEntitySign = tileEntitySign;
    this.signText = tileEntitySign.signText;
  }
}

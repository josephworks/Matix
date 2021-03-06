package de.paxii.clarinet.util.alt;

import de.paxii.clarinet.gui.menu.login.AltObject;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lars on 14.02.2016.
 */
public class AltContainer {
  @Getter
  @Setter
  private ArrayList<AltObject> altList;

  public AltContainer(ArrayList<AltObject> altList) {
    this.altList = altList;
  }
}

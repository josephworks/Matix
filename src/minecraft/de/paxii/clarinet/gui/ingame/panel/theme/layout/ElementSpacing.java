package de.paxii.clarinet.gui.ingame.panel.theme.layout;

import lombok.Data;

@Data
public class ElementSpacing {

  private int width;
  
  private int height;
  
  private int marginTop;

  private int marginRight;
  
  private int marginBottom;
  
  private int marginLeft;

  public ElementSpacing(int width, int height) {
    this(width, height, 0, 0, 0, 0);
  }

  public ElementSpacing(int width, int height, int marginTop, int marginRight, int marginBottom, int marginLeft) {
    this.width = width;
    this.height = height;
    this.marginTop = marginTop;
    this.marginRight = marginRight;
    this.marginBottom = marginBottom;
    this.marginLeft = marginLeft;
  }

}

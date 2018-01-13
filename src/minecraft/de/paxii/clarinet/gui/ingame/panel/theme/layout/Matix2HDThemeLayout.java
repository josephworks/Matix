package de.paxii.clarinet.gui.ingame.panel.theme.layout;

public class Matix2HDThemeLayout implements GuiThemeLayout {

  @Override
  public ElementSpacing getPanelLayout() {
    return new ElementSpacing(100, 200);
  }

  @Override
  public ElementSpacing getButtonLayout() {
    return new ElementSpacing(90, 12, 0, 0, 1, 5);
  }

  @Override
  public ElementSpacing getBlockButtonLayout() {
    return new ElementSpacing(20, 20, 0, 5, 5, 0);
  }

  @Override
  public ElementSpacing getSliderLayout() {
    return new ElementSpacing(88, 15, 0, 0, 1, 5);
  }
}

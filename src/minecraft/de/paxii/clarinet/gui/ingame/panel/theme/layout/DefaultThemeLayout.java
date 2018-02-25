package de.paxii.clarinet.gui.ingame.panel.theme.layout;

public class DefaultThemeLayout implements GuiThemeLayout {

  @Override
  public ElementSpacing getPanelLayout() {
    return new ElementSpacing(100, 200);
  }

  @Override
  public ElementSpacing getModuleButtonLayout() {
    return new ElementSpacing(90, 16, 2, 8, 0, 2);
  }

  @Override
  public ElementSpacing getButtonLayout() {
    return new ElementSpacing(90, 16, 2, 0, 0, 2);
  }

  @Override
  public ElementSpacing getBlockButtonLayout() {
    return new ElementSpacing(20, 20, 0, 5, 5, 0);
  }

  @Override
  public ElementSpacing getSliderLayout() {
    return new ElementSpacing(86, 15, 3, 0, 2, 7);
  }
}

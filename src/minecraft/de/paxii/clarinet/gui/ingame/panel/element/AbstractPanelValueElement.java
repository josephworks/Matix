package de.paxii.clarinet.gui.ingame.panel.element;

import lombok.Getter;

/**
 * Created by Lars on 31.07.2016.
 */
public abstract class AbstractPanelValueElement<T> extends PanelElement {
  @Getter
  protected T value;

  public void setValue(T newValue) {
    if (this.value != newValue || !this.value.equals(newValue)) {
      this.onUpdate(newValue, this.value);
    }
    this.value = newValue;
  }

  public void onUpdate(T newValue, T oldValue) {
  }
}

package de.paxii.clarinet.util.module.settings;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lars on 30.03.2016.
 */
public class ValueBaseSettingsObject {
  @Getter
  @Setter
  private float min, max, value;
  @Getter
  @Setter
  private String name;
  @Getter
  @Setter
  private boolean shouldRound;

  public ValueBaseSettingsObject(ValueBase valueBase) {
    this.name = valueBase.getName();
    this.value = valueBase.getValue();
    this.min = valueBase.getMin();
    this.max = valueBase.getMax();
    this.shouldRound = valueBase.isShouldRound();
  }
}

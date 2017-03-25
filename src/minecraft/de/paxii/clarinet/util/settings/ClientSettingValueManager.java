package de.paxii.clarinet.util.settings;

import de.paxii.clarinet.util.settings.type.ClientSettingInteger;
import de.paxii.clarinet.util.settings.type.ClientSettingLong;

/**
 * Created by Lars on 25.03.2017.
 */
public class ClientSettingValueManager {

  /**
   * FIXME Find some other way to handle this. Maybe using a JsonDeserializer?
   * This converts settings stored as doubles to int if the value stays the same
   * Gson uses Doubles rather than Integers when guessing data types.
   *
   * @param clientSetting ClientSetting to patch
   * @return patched ClientSetting
   */
  public static ClientSetting patchValue(ClientSetting clientSetting) {
    if (clientSetting.getValue() instanceof Double) {
      double doubleValue = (double) clientSetting.getValue();

      long longValue = (long) doubleValue;
      int intValue = (int) longValue;
      if (doubleValue == intValue) {
        return new ClientSettingInteger(clientSetting.getName(), intValue);
      } else if (doubleValue == longValue) {
        return new ClientSettingLong(clientSetting.getName(), longValue);
      }
    }

    return clientSetting;
  }

  public static Object patchValue(Object value) {
    if (value instanceof Double) {
      double doubleValue = (double) value;

      long longValue = (long) doubleValue;
      int intValue = (int) longValue;
      if (doubleValue == intValue) {
        return intValue;
      } else if (doubleValue == longValue) {
        return longValue;
      }
    }

    return value;
  }

}

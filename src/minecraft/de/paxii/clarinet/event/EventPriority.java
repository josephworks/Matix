package de.paxii.clarinet.event;

public class EventPriority {
  public static final byte HIGHEST = 0,
          HIGH = 1,
          NORMAL = 2,
          LOW = 3,
          LOWEST = 4;

  public static int[] getValues() {
    return new int[]{HIGHEST, HIGH, NORMAL, LOW, LOWEST};
  }

  public String toString(int eventPriority) {
    switch (eventPriority) {
      case HIGHEST:
        return "Highest";

      case HIGH:
        return "High";

      case NORMAL:
        return "Normal";

      case LOW:
        return "Low";

      case LOWEST:
        return "Lowest";

      default:
        return "Normal";
    }
  }
}

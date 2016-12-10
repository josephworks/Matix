package de.paxii.clarinet.util.notifications;

import lombok.Getter;

/**
 * Created by Lars on 26.05.2016.
 */
public enum NotificationPriority {
  NORMAL(0, 0xFFFFFFFF),
  GOOD(1, 0xFF80FF00),
  WARNING(2, 0xFFFFDD00),
  DANGER(3, 0xFFFF0000);

  @Getter
  private int priority;
  @Getter
  private int color;

  NotificationPriority(int priority, int color) {
    this.priority = priority;
    this.color = color;
  }
}

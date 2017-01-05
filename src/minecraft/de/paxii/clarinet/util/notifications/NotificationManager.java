package de.paxii.clarinet.util.notifications;

import de.paxii.clarinet.Wrapper;

import java.util.ArrayList;

import lombok.Getter;

/**
 * Created by Lars on 26.05.2016.
 */
public class NotificationManager {
  private final NotificationRenderer notificationRenderer;
  @Getter
  private ArrayList<Notification> notifications;

  public NotificationManager() {
    this.notifications = new ArrayList<>();
    this.notificationRenderer = new NotificationRenderer(this);

    Wrapper.getEventManager().register(this.notificationRenderer);
  }

  public void addNotification(String text) {
    this.getNotifications().add(new Notification(text));
  }

  public void addNotification(String text, long time) {
    this.getNotifications().add(new Notification(text, time));
  }

  public void addNotification(String text, NotificationPriority notificationPriority) {
    this.getNotifications().add(new Notification(text, notificationPriority));
  }

  public void addNotification(String text, NotificationPriority notificationPriority, long time) {
    this.getNotifications().add(new Notification(text, notificationPriority, time));
  }
}

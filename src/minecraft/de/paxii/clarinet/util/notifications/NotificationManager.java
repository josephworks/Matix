package de.paxii.clarinet.util.notifications;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.threads.ConcurrentArrayList;
import lombok.Getter;

/**
 * Created by Lars on 26.05.2016.
 */
public class NotificationManager {
	@Getter
	private ConcurrentArrayList<Notification> notifications;
	private final NotificationRenderer notificationRenderer;

	public NotificationManager() {
		this.notifications = new ConcurrentArrayList<>();
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

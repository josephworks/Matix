package de.paxii.clarinet.util.notifications;

import lombok.Getter;

/**
 * Created by Lars on 26.05.2016.
 */
public class Notification {
	private final long initialTime;
	@Getter
	private String text;
	@Getter
	private NotificationPriority priority;
	@Getter
	private long time;

	public Notification(String text) {
		this(text, NotificationPriority.NORMAL, 3000);
	}

	public Notification(String text, long time) {
		this(text, NotificationPriority.NORMAL, time);
	}

	public Notification(String text, NotificationPriority notificationPriority) {
		this(text, notificationPriority, 3000);
	}

	public Notification(String text, NotificationPriority notificationPriority, long time) {
		this.text = text;
		this.priority = notificationPriority;
		this.time = time;
		this.initialTime = System.currentTimeMillis();
	}

	public boolean shouldDisplay() {
		return System.currentTimeMillis() <= (this.initialTime + this.time);
	}
}

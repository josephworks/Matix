package de.paxii.clarinet.util.notifications;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.event.events.gui.RenderGuiScreenEvent;
import de.paxii.clarinet.util.objects.IntObject;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

/**
 * Created by Lars on 26.05.2016.
 */
public class NotificationRenderer {
	private NotificationManager notificationManager;
	private ArrayList<Notification> oldNotifications;

	public NotificationRenderer(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
		this.oldNotifications = new ArrayList<>();
	}

	@EventHandler
	private void onIngameTick(IngameTickEvent event) {
		this.renderNotifications();
	}

	@EventHandler
	private void onRenderGuiScreen(RenderGuiScreenEvent event) {
		this.renderNotifications();
	}

	private void renderNotifications() {
		if (!notificationManager.getNotifications().isEmpty()) {
			ScaledResolution scaledResolution = Wrapper.getScaledResolution();
			IntObject posY = new IntObject(20);

			this.notificationManager.getNotifications().forEach((notification -> {
				if (notification.shouldDisplay()) {
					int textWidth = Wrapper.getFontRenderer().getStringWidth(notification.getText());
					int posX = scaledResolution.getScaledWidth() / 2 - (textWidth / 2);

					Wrapper.getFontRenderer().drawString(notification.getText(), posX, posY.getInteger(), notification.getPriority().getColor());

					posY.add(15);
				} else {
					this.oldNotifications.add(notification);
				}
			}));

			this.notificationManager.getNotifications().removeAll(this.oldNotifications);
			this.oldNotifications.clear();
		}
	}
}

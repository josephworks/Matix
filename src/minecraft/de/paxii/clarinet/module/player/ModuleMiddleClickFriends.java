package de.paxii.clarinet.module.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

public class ModuleMiddleClickFriends extends Module {
	private final TimeManager timeManager;

	public ModuleMiddleClickFriends() {
		super("MiddleClick", ModuleCategory.PLAYER, -1);

		this.setDescription("Adds friends when you click the middle mouse button while aiming at them.");

		this.timeManager = new TimeManager();
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);
	}

	@EventHandler
	public void onTick(IngameTickEvent event) {
		this.timeManager.updateTimer();

		if (this.timeManager.sleep(500L)) {
			if (Mouse.isButtonDown(2)) {

				Entity pointedEntity = Wrapper.getMinecraft().pointedEntity;

				if (pointedEntity != null) {
					if (pointedEntity instanceof EntityPlayer) {
						String userName = pointedEntity.getName();

						if (!Wrapper.getFriendManager().isFriend(userName)) {
							Wrapper.getFriendManager().addFriend(userName);
							Chat.printClientMessage("Added Friend " + userName + ".");
						} else {
							Wrapper.getFriendManager().removeFriend(userName);
							Chat.printClientMessage("Removed Friend " + userName + ".");
						}
					}
				}

			}

			this.timeManager.updateLast();
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getEventManager().unregister(this);
	}
}

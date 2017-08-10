package de.paxii.clarinet.util.module.friends;

import de.paxii.clarinet.Wrapper;

import java.awt.*;
import java.util.HashMap;

import de.paxii.clarinet.event.EventManager;
import de.paxii.clarinet.event.events.client.AddFriendEvent;
import de.paxii.clarinet.event.events.client.RemoveFriendEvent;
import lombok.Setter;

public class FriendManager {
  @Setter
  private HashMap<String, Integer> friendList;

  public FriendManager() {
    this.friendList = new HashMap<>();
  }

  public boolean addFriend(String friendName) {
    return this.addFriend(friendName, 0x00FF00);
  }

  public boolean addFriend(String friendName, int friendColor) {
    if (!this.friendList.containsKey(friendName)) {
      this.friendList.put(friendName, friendColor);
      EventManager.call(new AddFriendEvent(friendName));
      return true;
    }

    return false;
  }

  public boolean removeFriend(String friendName) {
    if (this.friendList.containsKey(friendName)) {
      this.friendList.remove(friendName);
      EventManager.call(new RemoveFriendEvent(friendName));
      return true;
    }

    return false;
  }

  public boolean isFriend(String friendName) {
    return this.friendList.containsKey(friendName)
            || friendName.equals(Wrapper.getPlayer().getName());
  }

  public int getFriendColor(String friendName) {
    if (this.isFriend(friendName)) {
      return this.getFriends().get(friendName);
    }

    return 0xFF0000;
  }

  public Color getFriendColorObject(String friendName) {
    int friendColor = this.getFriendColor(friendName);

    return new Color(friendColor);
  }

  public HashMap<String, Integer> getFriends() {
    return this.friendList;
  }
}

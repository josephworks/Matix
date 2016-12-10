package de.paxii.clarinet.util.module.friends;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lars on 14.02.2016.
 */
public class FriendObjectContainer {
  @Getter
  @Setter
  private HashMap<String, Integer> friendList;

  public FriendObjectContainer(HashMap<String, Integer> friendList) {
    this.friendList = friendList;
  }
}

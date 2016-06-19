package de.paxii.clarinet.util.module.friends;

import lombok.Getter;

public class FriendObject {
	@Getter
	private String friendName;
	@Getter
	private int friendColor;

	public FriendObject(String friendName, int friendColor) {
		this.friendName = friendName;
		this.friendColor = friendColor;
	}
}

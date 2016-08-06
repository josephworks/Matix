package de.paxii.clarinet.command;

import lombok.Getter;

public enum CommandCategory {
	MAIN("Main"),
	MODULE("Module");

	@Getter
	private String name;

	CommandCategory(String name) {
		this.name = name;
	}

	public String getCategoryName() {
		return this.name;
	}

	public String toString() {
		return this.name;
	}
}

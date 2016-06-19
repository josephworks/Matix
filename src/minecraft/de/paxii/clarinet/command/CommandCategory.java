package de.paxii.clarinet.command;

public enum CommandCategory {
	MAIN,
	MODULE;

	public String getCategoryName() {
		switch (this) {
			case MAIN:
				return "Main";
			case MODULE:
				return "Module";
		}

		return null;
	}

	public String toString() {
		return this.getCategoryName();
	}
}

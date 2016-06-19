package de.paxii.clarinet.module;

import lombok.Getter;

public enum ModuleCategory {
	PLAYER(0xFFD31EC2), WORLD(0xFF1EC0A8), COMBAT(0xFFBB0711), RENDER(0xFFA3F330), MOVEMENT(0xFFDEA225), OTHER(0xFF239991);

	@Getter
	private int color;

	ModuleCategory(int color) {
		this.color = color;
	}

	@Override
	public String toString() {
		String curString = super.toString();
		return curString.charAt(0) + curString.substring(1).toLowerCase();
	}
}

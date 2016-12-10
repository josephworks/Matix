package de.paxii.clarinet.util.module.settings;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class ValueBase implements Comparable<ValueBase> {
	@Getter
	private static ArrayList<ValueBase> valueList = new ArrayList<>();
	@Getter
	@Setter
	private float min, max, value;
	@Getter
	@Setter
	private String name, displayName;
	@Getter
	@Setter
	private boolean shouldRound;

	public ValueBase(String name, float current, float min, float max) {
		this(name, current, min, max, false);
	}

	public ValueBase(String name, float current, float min, float max, String displayName) {
		this(name, current, min, max, false, displayName);
	}

	public ValueBase(String name, float current, float min, float max, boolean shouldRound) {
		this(name, current, min, max, shouldRound, null);
	}

	public ValueBase(String name, float current, float min, float max, boolean shouldRound, String displayName) {
		this.name = name;
		this.value = current;
		this.min = min;
		this.max = max;
		this.shouldRound = shouldRound;
		this.displayName = displayName;

		if (!doesValueExist(getName())) {
			valueList.add(this);
		}
	}

	public static boolean doesValueExist(String name) {
		for (ValueBase vb : valueList) {
			if (vb.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public static ValueBase getValueBase(String name) {
		for (ValueBase vb : valueList) {
			if (vb.getName().equals(name)) {
				return vb;
			}
		}

		return null;
	}

	public String getDisplayName() {
		return this.displayName != null ? this.displayName : this.name;
	}

	public void onUpdate(float oldValue, float newValue) {
	}

	public void setValue(float value) {
		if (this.value != value) {
			this.onUpdate(this.value, value);
		}
		this.value = value;
	}

	@Override
	public int compareTo(ValueBase vb) {
		return this.getName().compareToIgnoreCase(vb.getName());
	}
}

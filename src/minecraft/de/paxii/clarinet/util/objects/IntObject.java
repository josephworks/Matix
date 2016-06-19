package de.paxii.clarinet.util.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lars on 26.05.2016.
 */
public class IntObject {
	@Getter
	@Setter
	private int integer;

	public IntObject(int integer) {
		this.integer = integer;
	}

	public IntObject add(int integer) {
		this.integer += integer;

		return this;
	}

	public IntObject substract(int integer) {
		this.integer -= integer;

		return this;
	}

	public IntObject multiply(int integer) {
		this.integer *= integer;

		return this;
	}

	public IntObject divide(int integer) {
		this.integer /= integer;

		return this;
	}
}

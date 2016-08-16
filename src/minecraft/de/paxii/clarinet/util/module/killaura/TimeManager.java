package de.paxii.clarinet.util.module.killaura;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class TimeManager {
	private final Random utilRandom;
	private int currentRandomness;

	@Getter
	@Setter
	private long current, last;
	@Getter
	@Setter
	private int randomness;
	@Getter
	@Setter
	private boolean random;

	public TimeManager() {
		this.utilRandom = new Random();
		createNewRandomDelay();
	}

	public void updateTimer() {
		this.current = getCurrentMillis();
	}

	public void updateTimer(int newRandomness) {
		this.randomness = newRandomness;

		this.updateTimer();
	}

	public boolean sleep(final long delay) {
		boolean slept = current - last > delay + this.currentRandomness;

		if (slept) {
			createNewRandomDelay();
		}

		return slept;
	}

	public final void updateLast() {
		this.last = getCurrentMillis();
	}

	public long getCurrentMillis() {
		return (System.nanoTime() / 1000000);
	}

	private void createNewRandomDelay() {
		if (this.isRandom()) {
			this.currentRandomness = this.utilRandom.nextInt(this.randomness == 0 ? 200 : this.randomness);
		}
	}
}

package de.paxii.clarinet.util.threads;

import java.util.ArrayList;

/**
 * Created by Lars on 05.06.2016.
 */
public class ThreadChain {
	private ArrayList<Thread> threadList;
	
	public ThreadChain() {
		this.threadList = new ArrayList<>();
	}

	public ThreadChain chainThread(Thread thread) {
		this.threadList.add(thread);

		return this;
	}

	public void next() {
		if (this.threadList.size() > 0) {
			this.threadList.get(0).start();
			this.threadList.remove(0);
		}
	}

	public void kickOff() {
		this.next();
	}
}

package de.paxii.clarinet.event;

import java.lang.reflect.Method;

class MethodData {
	private final Object source;
	private final Method target;
	private final int priority;

	public MethodData(Object source, Method target, int i) {
		this.source = source;
		this.target = target;
		this.priority = i;
	}

	public Object getSource() {
		return source;
	}

	public Method getTarget() {
		return target;
	}

	public int getPriority() {
		return priority;
	}
}

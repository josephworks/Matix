package de.paxii.clarinet.event;

import de.paxii.clarinet.event.events.Event;
import de.paxii.clarinet.event.events.game.KeyPressedEvent;
import de.paxii.clarinet.event.events.type.EventCancellable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
	private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY_MAP = new HashMap<Class<? extends Event>, List<MethodData>>();

	public void register(Object object) {
		for (final Method method : object.getClass().getDeclaredMethods()) {
			if (isMethodBad(method)) {
				continue;
			}

			register(object, method);
		}
	}

	public void register(Object object, Class<? extends Event> eventClass) {
		for (final Method method : object.getClass().getDeclaredMethods()) {
			if (isMethodBad(method, eventClass)) {
				continue;
			}

			register(object, method);
		}
	}

	public void unregister(Object object) {
		for (final List<MethodData> dataList : REGISTRY_MAP.values()) {
			for (final MethodData data : dataList) {
				if (data.getSource().equals(object)) {
					dataList.remove(data);
				}
			}
		}

		cleanMap(true);
	}

	public void unregister(Object object, Class<? extends Event> eventClass) {
		if (REGISTRY_MAP.containsKey(eventClass)) {
			for (final MethodData data : REGISTRY_MAP.get(eventClass)) {
				if (data.getSource().equals(object)) {
					REGISTRY_MAP.get(eventClass).remove(data);
				}
			}

			cleanMap(true);
		}
	}

	private void register(Object object, Method method) {
		Class<? extends Event> indexClass = (Class<? extends Event>) method
				.getParameterTypes()[0];
		final MethodData methodData = new MethodData(object, method, method
				.getAnnotation(EventHandler.class).priority());
		methodData.getTarget().setAccessible(true);

		if (REGISTRY_MAP.containsKey(indexClass)) {
			if (!REGISTRY_MAP.get(indexClass).contains(methodData)) {
				REGISTRY_MAP.get(indexClass).add(methodData);
				sortListValue(indexClass);
			}
		} else {
			REGISTRY_MAP.put(indexClass,
					new CopyOnWriteArrayList<MethodData>() {
						private static final long serialVersionUID = 0L;

						{
							add(methodData);
						}
					});
		}
	}

	private void sortListValue(Class<? extends Event> indexClass) {
		List<MethodData> sortedList = new CopyOnWriteArrayList<MethodData>();

		for (final int priority : EventPriority.getValues()) {
			for (final MethodData data : REGISTRY_MAP.get(indexClass)) {
				if (data.getPriority() == priority) {
					sortedList.add(data);
				}
			}
		}

		REGISTRY_MAP.put(indexClass, sortedList);
	}

	public static void cleanMap(boolean onlyEmptyEntries) {
		Iterator<Map.Entry<Class<? extends Event>, List<MethodData>>> mapIterator = REGISTRY_MAP
				.entrySet().iterator();

		while (mapIterator.hasNext()) {
			if (!onlyEmptyEntries || mapIterator.next().getValue().isEmpty()) {
				mapIterator.remove();
			}
		}
	}

	private static boolean isMethodBad(Method method) {
		return method.getParameterTypes().length != 1
				|| !method.isAnnotationPresent(EventHandler.class);
	}

	private static boolean isMethodBad(Method method,
	                                   Class<? extends Event> eventClass) {
		return isMethodBad(method)
				|| !method.getParameterTypes()[0].equals(eventClass);
	}

	private static Event lastCalledEvent;
	private static long lastCalledMS;

	public static final Event call(final Event event) {
		if (event instanceof KeyPressedEvent) {
			if (lastCalledMS != 0) {
				if (lastCalledMS + 10 >= System.currentTimeMillis()) {
					if (((KeyPressedEvent) event).getKey() == ((KeyPressedEvent) lastCalledEvent)
							.getKey()) {
						return event;
					}
				}
			} else {
				lastCalledEvent = event;
				lastCalledMS = System.currentTimeMillis();
			}
		}

		List<MethodData> dataList = REGISTRY_MAP.get(event.getClass());

		if (dataList != null) {
			if (event instanceof EventCancellable) {
				EventCancellable cancellable = (EventCancellable) event;

				for (final MethodData data : dataList) {
					invoke(data, event);

					if (cancellable.isCancelled()) {
						break;
					}
				}
			} else {
				for (final MethodData data : dataList) {
					invoke(data, event);
				}
			}
		}

		return event;
	}

	private static void invoke(MethodData data, Event argument) {
		try {
			data.getTarget().invoke(data.getSource(), argument);
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
	}
}

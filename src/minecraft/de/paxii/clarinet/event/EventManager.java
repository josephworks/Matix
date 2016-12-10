package de.paxii.clarinet.event;

import de.paxii.clarinet.event.events.Event;
import de.paxii.clarinet.event.events.game.KeyPressedEvent;
import de.paxii.clarinet.event.events.type.EventCancellable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
	private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY_MAP = new HashMap<>();
	private static KeyPressedEvent lastCalledEvent;
	private static long lastCalledMS;

	private static void cleanMap(boolean onlyEmptyEntries) {
		Iterator<Map.Entry<Class<? extends Event>, List<MethodData>>> mapIterator = REGISTRY_MAP.entrySet().iterator();

		while (mapIterator.hasNext()) {
			if (!onlyEmptyEntries || mapIterator.next().getValue().isEmpty()) {
				mapIterator.remove();
			}
		}
	}

	private static boolean isMethodBad(Method method) {
		return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventHandler.class);
	}

	private static boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
		return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
	}

	public static <T extends Event> T call(final T event) {
		if (event instanceof KeyPressedEvent) {
			KeyPressedEvent keyPressedEvent = (KeyPressedEvent) event;

			if (lastCalledMS != 0) {
				if (lastCalledMS + 10 >= System.currentTimeMillis()) {
					if (keyPressedEvent.getKey() == lastCalledEvent.getKey()) {
						return event;
					}
				}
			} else {
				lastCalledEvent = keyPressedEvent;
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
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

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
			dataList.stream()
			        .filter(data -> data.getSource().equals(object))
			        .forEach(dataList::remove);
		}

		cleanMap(true);
	}

	public void unregister(Object object, Class<? extends Event> eventClass) {
		if (REGISTRY_MAP.containsKey(eventClass)) {
			REGISTRY_MAP.get(eventClass).stream()
			            .filter(data -> data.getSource().equals(object))
			            .forEach(REGISTRY_MAP.get(eventClass)::remove);

			cleanMap(true);
		}
	}

	private void register(Object object, Method method) {
		Class<? extends Event> indexClass = (Class<? extends Event>) method.getParameterTypes()[0];
		final MethodData methodData = new MethodData(object, method, method.getAnnotation(EventHandler.class).priority());
		methodData.getTarget().setAccessible(true);

		if (REGISTRY_MAP.containsKey(indexClass)) {
			if (!REGISTRY_MAP.get(indexClass).contains(methodData)) {
				REGISTRY_MAP.get(indexClass).add(methodData);
				sortListValue(indexClass);
			}
		} else {
			CopyOnWriteArrayList<MethodData> methodDataList = new CopyOnWriteArrayList<>(new MethodData[]{ methodData });
			REGISTRY_MAP.put(indexClass, methodDataList);
		}
	}

	private void sortListValue(Class<? extends Event> indexClass) {
		List<MethodData> sortedList = new CopyOnWriteArrayList<>();

		for (final int priority : EventPriority.getValues()) {
			REGISTRY_MAP.get(indexClass).stream()
			            .filter(data -> data.getPriority() == priority)
			            .forEach(sortedList::add);
		}

		REGISTRY_MAP.put(indexClass, sortedList);
	}
}
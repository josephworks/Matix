package de.paxii.clarinet.event;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

	int priority() default EventPriority.NORMAL;
}

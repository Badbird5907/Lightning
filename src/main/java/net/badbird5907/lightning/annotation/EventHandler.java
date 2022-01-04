package net.badbird5907.lightning.annotation;

import net.badbird5907.lightning.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    byte value() default EventPriority.NORMAL;

    /**
     * if true, don't call the event handler if the event is cancelled
     * @return
     */
    boolean ignoreCancelled() default false;
}

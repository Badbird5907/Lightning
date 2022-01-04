package net.badbird5907.lightning.internal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.badbird5907.lightning.annotation.EventHandler;
import net.badbird5907.lightning.event.Event;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@Getter
public class EventInfo {
    private final Method method;
    private final Class<? extends Event> eventType;
    private final int priority;
    private final EventHandler listenerAnnotation;
    private final Object instance;

    @Override
    public String toString() {
        return "EventInfo{" +
                "method=" + method +
                ", eventType=" + eventType +
                ", priority=" + priority +
                ", listenerAnnotation=" + listenerAnnotation +
                ", instance=" + instance +
                '}';
    }
}

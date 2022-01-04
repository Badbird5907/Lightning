package net.badbird5907.lightning.internal;

import net.badbird5907.lightning.annotation.EventHandler;
import net.badbird5907.lightning.event.Event;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface IEventBus {
    void register(Class<?> event);

    void register(Object listener);

    void register(EventHandler handler, Method method, Object instance);

    default void register(EventHandler handler, Method method) {
        register(handler, method, null);
    }

    Map<Class<? extends Event>, List<EventInfo>> getListeners();

    void call(Event event);

    default void callEvent(Event event) {
        call(event);
    }

    default void post(Event event) {
        call(event);
    }
}

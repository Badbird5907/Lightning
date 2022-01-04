package net.badbird5907.lightning;

import lombok.Getter;
import net.badbird5907.lightning.annotation.EventHandler;
import net.badbird5907.lightning.event.Cancellable;
import net.badbird5907.lightning.event.Event;
import net.badbird5907.lightning.internal.EventInfo;
import net.badbird5907.lightning.internal.IEventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus implements IEventBus {
    @Getter
    private Map<Class<? extends Event>, List<EventInfo>> listeners;

    @Getter
    private Logger logger;

    @Getter
    private EventBusSettings settings;

    public EventBus(EventBusSettings settings) {
        this.settings = settings;
        if (settings.isUseConcurrentHashMap()) { //we have this setting so users can get ultimate performance
            listeners = new ConcurrentHashMap<>();
        } else listeners = new HashMap<>();
        logger = settings.getLogger();
    }

    public EventBus() {
        this(new EventBusSettings());
    }

    @Override
    public void register(Class<?> event) {
        debug("Registering event class " + event.getSimpleName());
        for (Method method : event.getDeclaredMethods()) {
            debug("Found method " + method.getName());
            if (Modifier.isStatic(method.getModifiers())) { //static methods
                debug("Method is static");
                if (method.isAnnotationPresent(EventHandler.class)) {
                    debug("Method is annotated with EventHandler");
                    register(method.getAnnotation(EventHandler.class), method);
                }
            }else debug("Method is not static, ignoring");
        }
    }

    @Override
    public void register(Object listener) {
        debug("Registering listener " + listener.getClass().getSimpleName());
        for (Method method : listener.getClass().getDeclaredMethods()) {
            debug("Found method " + method.getName());
            if (method.isAnnotationPresent(EventHandler.class)) {
                debug("Method is annotated with EventHandler");
                register(method.getAnnotation(EventHandler.class), method, listener);
            }
        }
    }

    @Override
    public void register(EventHandler handler, Method method, Object instance) {
        debug("Registering method " + method.getName() + " in class " + method.getDeclaringClass().getSimpleName() + " static: " + (instance == null));
        debug("Parameter types: " + Arrays.asList(method.getParameterTypes()).toString());
        if (method.getParameterTypes().length != 1) {
            throw new IllegalArgumentException("Method " + method.getName() + " must have one parameter");
        }
        //check if parameter implements Event
        if (!Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
            throw new IllegalArgumentException("Method " + method.getName() + " must have one parameter that implements Event");
        }
        Class<? extends Event> type = (Class<? extends Event>) method.getParameterTypes()[0];
        List<EventInfo> eventInfos = listeners.get(method.getParameterTypes()[0]);
        if (eventInfos == null) {
            debug("eventInfos is null");
            eventInfos = new ArrayList<>();
        }else {
            for (EventInfo eventInfo : eventInfos) { //TODO: Still slightly buggy, but shouldn't be a problem unless you're intentionally registering the same class as a class and instance, and the class has both static and instance event handlers
                //check if method is already registered
                if (eventInfo.getMethod().equals(method)) {
                    debug("Method " + method.getName() + " is already registered");
                    return;
                }
                if (instance != null && Modifier.isStatic(eventInfo.getMethod().getModifiers())) {
                    //check if parent class is the same
                    if (eventInfo.getMethod().getDeclaringClass().equals(method.getDeclaringClass())) {
                        debug("Method " + method.getName() + " is already registered, ignoring");
                        return;
                    }
                }
            }
        }
        EventInfo info = new EventInfo(method, type, handler.value(), handler, instance);
        debug("Adding event info " + info.toString());
        eventInfos.add(info);
        listeners.put(type, eventInfos);
    }

    @Override
    public void call(Event event) {
        listeners.forEach((type, eventInfos) -> {
            if (event.getClass().isAssignableFrom(type)) {
                eventInfos.forEach(eventInfo -> {
                    try {
                        boolean cancellable = event instanceof Cancellable;
                        if (cancellable && ((Cancellable) event).isCancelled()) {
                            if (eventInfo.getListenerAnnotation().ignoreCancelled()) {
                                return;
                            }
                        }
                        eventInfo.getMethod().setAccessible(true);
                        eventInfo.getMethod().invoke(eventInfo.getInstance(), event);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        logger.error("Error passing event to method " + eventInfo.getMethod().getName() + " in class " + eventInfo.getInstance().getClass().getName());
                        e.printStackTrace();
                    }
                });
            }
        });
    }
    void debug(String message){
        if (settings.isDebugMessages()){
            logger.debug(message);
        }
    }

    @Getter
    public static class EventBusSettings {
        private boolean useConcurrentHashMap = true,debugMessages = false;
        private Logger logger = new DefaultLogger();
        public EventBusSettings setUseConcurrentHashMap(boolean useConcurrentHashMap) {
            this.useConcurrentHashMap = useConcurrentHashMap;
            return this;
        }
        public EventBusSettings setLogger(Logger logger) {
            this.logger = logger;
            return this;
        }
        public EventBusSettings setDebugMessages(boolean debugMessages) {
            this.debugMessages = debugMessages;
            return this;
        }
    }
    public interface Logger {
        void log(String message);
        void debug(String message);
        void error(String message);
    }
    public static class DefaultLogger implements Logger {
        @Override
        public void debug(String message) {
            log("[Debug] " + message);
        }

        @Override
        public void log(String message) {
            System.out.println("[Lightning] " + message);
        }

        @Override
        public void error(String message) {
            System.err.println("[Lightning] " + message);
        }
    }
}

package net.badbird5907.lightning;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.badbird5907.lightning.annotation.EventHandler;
import net.badbird5907.lightning.event.Event;

public class Lightning {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        EventBus bus = new EventBus(new EventBus.EventBusSettings().setDebugMessages(true).setUseConcurrentHashMap(true));
        bus.register(TestListener.class);
        bus.register(new TestListener());
        bus.register(new SecondTestListener());

        bus.post(new TestEvent("deez nuts"));
    }
    private static class TestListener {

        @EventHandler
        public void onEvent0(TestEvent e) {
            System.out.println("a " + e.getMessage());
        }
        @EventHandler
        public static void onEvent1(TestEvent e) {
            System.out.println("b " + e.getMessage());
        }
    }
    private static class SecondTestListener {
        @EventHandler
        public void onEvent(TestEvent e) {
            System.out.println("c " + e.getMessage());
        }
    }
    @RequiredArgsConstructor
    @Getter
    private static class TestEvent implements Event {
        private final String message;
    }
}

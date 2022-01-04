package net.badbird5907.lightning;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.badbird5907.lightning.annotation.EventHandler;
import net.badbird5907.lightning.event.Cancellable;
import net.badbird5907.lightning.event.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LightningTest {
    @Test
    void testEventBus() {
        Stopwatch init = new Stopwatch();
        System.out.println("Hello World!");
        init.start();
        EventBus bus = new EventBus(new EventBus.EventBusSettings().setDebugMessages(true).setUseConcurrentHashMap(true));
        init.end();
        System.out.println("Initialization took " + init.getTotal() + "ms");
        Stopwatch register = new Stopwatch();
        register.start();
        bus.register(TestListener.class);
        register.startEnd();
        bus.register(new TestListener());
        register.startEnd();
        bus.register(new SecondTestListener());
        register.end();
        System.out.println("Registering took " + register.getTotal() + "ms with an average of " + register.getAverage() + "ms per listener");

        Stopwatch event = new Stopwatch();
        event.start();
        bus.post(new TestEvent("test"));
        event.startEnd();
        bus.post(new TestEvent("test 1"));
        event.startEnd();
        bus.post(new TestEvent("test 2"));
        event.end();

        System.out.println("Posting took " + event.getTotal() + "ms with an average of " + event.getAverage() + "ms per event");
    }
    @Test
    void testCancel() {
        EventBus bus = new EventBus(new EventBus.EventBusSettings().setDebugMessages(true).setUseConcurrentHashMap(true));
        bus.register(new TestListener());
        bus.register(new SecondTestListener());
        TestEvent event = new TestEvent("test");
        bus.post(event);
        Assertions.assertTrue(event.isCancelled());
    }

    private static class TestListener {
        @EventHandler
        public void onEvent0(TestEvent e) {
            System.out.println("a " + e.getMessage());
        }
        @EventHandler
        public static void onEvent1(TestEvent e) {
            e.setCancelled(true);
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
    private static class TestEvent extends Cancellable.DefaultCancellable implements Event {
        private final String message;
    }
}

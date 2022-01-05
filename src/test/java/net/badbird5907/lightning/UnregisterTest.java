package net.badbird5907.lightning;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.badbird5907.lightning.annotation.EventHandler;
import net.badbird5907.lightning.event.Cancellable;
import net.badbird5907.lightning.event.Event;
import net.badbird5907.lightning.event.EventPriority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UnregisterTest {
    @Test
    void main() {
        EventBus bus = new EventBus(new EventBus.EventBusSettings().setUseConcurrentHashMap(true).setDebugMessages(true));
        //bus.register(TestListener.class);
        bus.register(new SecondTestListener());
        bus.register(new FirstTestListener());
        TestEvent e = new TestEvent("test");
        bus.post(e);
        Assertions.assertEquals(e.getMessage(),"b");
        bus.unregister(FirstTestListener.class);
        TestEvent e2 = new TestEvent("test");
        bus.post(e2);
        Assertions.assertEquals(e2.getMessage(),"a");
    }
    private static class SecondTestListener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onEvent(TestEvent e) {
            System.out.println("a " + e.getMessage());
            e.setMessage("a");
        }
    }
    private static class FirstTestListener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onEvent(TestEvent e) {
            System.out.println("b " + e.getMessage());
            e.setMessage("b");
        }
    }
    @AllArgsConstructor
    @Getter
    @Setter
    private static class TestEvent extends Cancellable.DefaultCancellable implements Event {
        private String message;
    }
}

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

class ExecutionTimeTest {
    @Test
    void main() {
        EventBus bus = new EventBus(new EventBus.EventBusSettings().setUseConcurrentHashMap(true).setDebugMessages(false));
        //bus.register(TestListener.class);
        bus.register(new SecondTestListener());
        bus.register(new FirstTestListener());
        for (int i = 0; i < 10000; i++) {
            bus.post(new TestEvent());
        }
    }
    private static class SecondTestListener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onEvent(TestEvent e) {
        }
    }
    private static class FirstTestListener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onEvent(TestEvent e) {
        }
    }
    private static class TestEvent extends Cancellable.DefaultCancellable implements Event {
    }
}

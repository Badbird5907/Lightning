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

class PriorityTest {
    @Test
    void main() {
        EventBus bus = new EventBus(new EventBus.EventBusSettings().setUseConcurrentHashMap(true).setDebugMessages(true));
        //bus.register(TestListener.class);
        bus.register(new SecondTestListener());
        TestEvent e = new TestEvent("test");
        bus.post(e);
        Assertions.assertEquals(e.getMessage(),"e");
    }
    private static class SecondTestListener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onEvent(TestEvent e) {
            System.out.println("a " + e.getMessage());
            e.setMessage("a");
        }
        @EventHandler
        public void onEvent3(TestEvent e) {
            System.out.println("b " + e.getMessage());
            e.setMessage("b");
        }
        @EventHandler(priority = EventPriority.LOW)
        public void onEvent4(TestEvent e) {
            System.out.println("c " + e.getMessage());
            e.setMessage("c");
        }
        @EventHandler(priority = -75)
        public void onCustomPriority(TestEvent e) {
            System.out.println("d " + e.getMessage());
            e.setMessage("d");
        }
        @EventHandler(priority = EventPriority.LOWEST)
        public void onEvent2(TestEvent e) {
            System.out.println("e " + e.getMessage());
            e.setMessage("e");
        }
    }
    @AllArgsConstructor
    @Getter
    @Setter
    private static class TestEvent extends Cancellable.DefaultCancellable implements Event {
        private String message;
    }
}

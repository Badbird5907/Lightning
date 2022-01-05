package net.badbird5907.lightning;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.badbird5907.lightning.annotation.EventHandler;
import net.badbird5907.lightning.event.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MultipleEventsTest {
    @Test
    void test() {
        EventBus bus = new EventBus();
        bus.register(new Listener());
        TestEvent testEvent = new TestEvent("test");
        TestEvent2 testEvent2 = new TestEvent2("test2");
        bus.post(testEvent);
        bus.post(testEvent2);
        Assertions.assertSame("a", testEvent.getMessage());
        Assertions.assertSame("b", testEvent2.getMessage());
    }
    private class Listener {
        @EventHandler
        public void onEvent(TestEvent event) {
            event.setMessage("a");
        }
        @EventHandler
        public void onEvent2(TestEvent2 event) {
            event.setMessage("b");
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    private class TestEvent implements Event {
        private String message;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    private class TestEvent2 implements Event {
        private String message;
    }

}

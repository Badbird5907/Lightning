![Lightning](https://user-images.githubusercontent.com/50347938/148137125-eb762961-b68e-461e-9074-b3efd6390791.png)
# Lightning
## A lightning fast and lightweight event bus for Java

### Usage

```java
public class Lightning {
    public static void main(String[] args) {
        EventBus bus = new EventBus(); // create an event bus
        bus.register(new EventListener()); // register an event listener
        bus.register(EventListenerStatic.class); // register an event listener, using the class
        TestEvent testEvent = new TestEvent("test"); // create a test event that implements the Event interface and Cancellable interface
        bus.post(testEvent); // post the test event to the event bus
        if (testEvent.isCancelled()) { // check if the event was cancelled
            return; // if it was, return
        }
        assert testEvent.getMessage().equals("Goodbye World!");
        // assert that the message is "Goodbye World!" because the event listener with the lowest priority was called 
        // last and it overrides the previous event listener
    }
}
public class EventListener {
    @EventHandler(priority = EventPriority.HIGHEST) // register the event listener with the highest priority
    public void onEvent(TestEvent event) {
        System.out.println("Event received! " + event.getMessage()); // print a message to the console
        //The output will be "Event received! test" because the event listener with the highest priority will be called first
        event.setMessage("Hello World!"); // change the message of the event
    }
}
public class EventListenerStatic {
    @EventHandler // register the event listener with normal priority
    public static void onEvent(TestEvent event) {
        System.out.println("Event received! " + event.getMessage()); // print a message to the console
        //the output will be "Event received! Hello World!" because the event listener with the highest priority was called first
        event.setMessage("Goodbye World!"); // change the message of the event
    }
}
public class TestEvent implements Event, Cancellable {
    private String message;
    private boolean cancelled;
    public TestEvent(String message) {
        this.message = message;
    }
    //your getters and setters here
}
```

#### Dependency
If you use apache maven:
```xml
<dependency>
  <groupId>net.badbird5907</groupId>
  <artifactId>Lightning</artifactId>
  <version>1.0.0-REL</version>
</dependency>
```
Or alternatively, if you use gradle:
```groovy
repositories {
    mavenCentral()
}
dependencies {
    implementation 'net.badbird5907:Lightning:1.0.0-REL'
}
```

[Maven Search](https://search.maven.org/artifact/net.badbird5907/Lightning/1.0.0-REL/jar)

### Building
Lightning uses Gradle to build and test.

#### Requirements
 - Java 8 JDK or later
 - Git

#### Compiling 
```
git clone https://github.com/Badbird-5907/Lightning.git
cd Lightning
./gradlew build
```

### Dependencies
- None, Lightning is a pure Java library that can be used in any Java project!

### MIT License
[See Here](https://github.com/Badbird-5907/Lightning/blob/master/LICENSE.txt)
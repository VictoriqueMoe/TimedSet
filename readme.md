# timed set in Java

A simple timed set that will evict items after they have been added to the set.

Usage:

```java
final var timedSet = new TimedSet<>(1000);
timedSet.

add(1);
Thread.

sleep(1050);
timedSet.

add(2);
timedSet.

contains(1); // false
timedSet.

contains(2); // true
Thread.

sleep(1050);
timedSet.

contains(2); // false
```

Also supports refreshing an item.

The Timed Set fully implements the Set interface

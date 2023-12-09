package moe.victorique;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TimedSetTest {
  private TimedSet<Integer> set;

  @BeforeEach
  public void setup() {
    this.set = new TimedSet<>(1000);
  }

  @Test
  public void shouldRemoveAfter1Second() throws InterruptedException {
    set.add(1);
    assertEquals(1, set.size());
    Thread.sleep(1050);
    assertTrue(set.isEmpty());
  }

  @Test
  public void shouldRefreshItem() throws InterruptedException {
    final var set = new TimedSet<Integer>(1000);
    final var item = 1;
    set.add(item);
    assertEquals(set.size(), 1);
    Thread.sleep(500);
    set.refresh(item);
    assertTrue(set.getTimeRemaining(item) > 500);
  }

  @Test
  public void shouldRemove2Items() throws InterruptedException {
    final var item = 1;
    final var item2 = 2;

    // add first item
    set.add(item);
    Thread.sleep(500);

    // add send item after 500ms
    set.add(item2);

    // ensure the 2nd item added has a longer time left than the first
    assertTrue(set.getTimeRemaining(item) < set.getTimeRemaining(item2));
    Thread.sleep(500);

    // after 1 second in total, the first item should be removed, but not the 2nd
    assertFalse(set.contains(item));
    assertTrue(set.contains(item2));
    Thread.sleep(500);

    // now set should be empty
    assertTrue(set.isEmpty());
  }

  @Test
  public void shouldBeASet() {
    assertInstanceOf(Set.class, set);
  }
}

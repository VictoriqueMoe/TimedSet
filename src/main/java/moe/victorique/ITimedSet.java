package moe.victorique;

import java.util.Set;

/**
 * A set like object that evicts entries from the set after they have been in there for the set time
 */
interface ITimedSet<T> extends Set<T> {

  /**
   * Get the time left until this item is removed from the set
   */
  long getTimeRemaining(T item);

  /**
   * Refresh the timeout for this element (resets the timer for the items eviction)
   */
  boolean refresh(T item);
}

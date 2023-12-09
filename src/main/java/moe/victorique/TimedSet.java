package moe.victorique;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimedSet<T> implements ITimedSet<T> {

  private final Map<T, ScheduledFuture<?>> map = new HashMap<>();
  private final long timeout;
  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

  public TimedSet(long timeout) {
    this.timeout = timeout;
  }

  @Override
  public int size() {
    return this.map.size();
  }

  @Override
  public boolean isEmpty() {
    return this.map.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return asSet().contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return asSet().iterator();
  }

  @Override
  public Object[] toArray() {
    return asSet().toArray();
  }

  @Override
  public <T1> T1[] toArray(T1[] t1s) {
    return asSet().toArray(t1s);
  }

  @Override
  public boolean add(T key) {
    final var executor = executorService.schedule(() -> {
      this.map.remove(key);
    }, this.timeout, TimeUnit.MILLISECONDS);
    return this.map.put(key, executor) != null;
  }

  @Override
  public boolean remove(Object o) {
    if (!this.map.containsKey(o)) {
      return false;
    }
    final var executor = this.map.get(o);
    executor.cancel(true);
    return this.map.remove(o) != null;
  }

  @Override
  public boolean containsAll(Collection<?> collection) {
    return this.asSet().containsAll(collection);
  }

  @Override
  public boolean addAll(Collection<? extends T> collection) {
    final var sizeBefore = Integer.valueOf(this.map.size());
    for (T item : collection) {
      this.add(item);
    }
    return this.map.size() != sizeBefore;
  }

  @Override
  public boolean retainAll(Collection<?> collection) {
    var changed = false;
    var size = this.size();
    final var it = this.map.keySet().iterator();
    while (size-- > 0) {
      if (!collection.contains(it.next())) {
        it.remove();
        changed = true;
      }
    }
    return changed;
  }

  @Override
  public boolean removeAll(Collection<?> collection) {
    final var sizeBefore = Integer.valueOf(this.map.size());
    for (Object o : collection) {
      this.map.remove(o);
    }
    return this.map.size() != sizeBefore;
  }

  @Override
  public void clear() {
    this.map.clear();
  }

  @Override
  public long getTimeRemaining(T item) {
    return this.map.get(item).getDelay(TimeUnit.MILLISECONDS);
  }

  @Override
  public boolean refresh(T key) {
    if (!this.map.containsKey(key)) {
      return false;
    }
    final var executor = this.map.get(key);
    executor.cancel(true);
    return this.add(key);
  }

  private Set<T> asSet() {
    return this.map.keySet();
  }
}

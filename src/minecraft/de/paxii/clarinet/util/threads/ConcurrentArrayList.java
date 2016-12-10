package de.paxii.clarinet.util.threads;

import java.util.ArrayList;

public class ConcurrentArrayList<E> extends ArrayList<E> {

  @Override
  public synchronized boolean add(E value) {
    return super.add(value);
  }

  @Override
  public synchronized void clear() {
    super.clear();
  }

  @Override
  public synchronized E get(int index) {
    return super.get(index);
  }

  @Override
  public synchronized boolean contains(Object object) {
    return super.contains(object);
  }

  @Override
  public synchronized E remove(int index) {
    return super.remove(index);
  }

  @Override
  public synchronized boolean remove(Object object) {
    return super.remove(object);
  }

  @Override
  public synchronized int size() {
    return super.size();
  }
}

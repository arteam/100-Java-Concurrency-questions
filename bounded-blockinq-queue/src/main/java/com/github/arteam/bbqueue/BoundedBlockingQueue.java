package com.github.arteam.bbqueue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BoundedBlockingQueue<T> implements BlockingQueue<T> {

    private final ArrayDeque<T> deque;
    private final int limit;

    public BoundedBlockingQueue(int limit) {
        this.limit = limit;
        deque = new ArrayDeque<T>(limit);
    }

    @Override
    public synchronized boolean add(T t) {
        if (!offer(t)) {
            throw new IllegalStateException("Queue is full");
        }
        return true;
    }

    @Override
    public synchronized boolean offer(T t) {
        return deque.size() < limit && addAndNotify(t);
    }

    @Override
    public synchronized void put(T t) throws InterruptedException {
        while (deque.size() >= limit) {
            wait();
        }
        addAndNotify(t);
    }

    @Override
    public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
        int enters = 0;
        synchronized (this) {
            while (deque.size() >= limit) {
                if (enters++ > 0) {
                    return false;
                }
                wait(unit.toMillis(timeout));
            }
            return addAndNotify(t);
        }
    }

    private boolean addAndNotify(T t) {
        deque.add(t);
        notifyAll();
        return true;
    }

    @Override
    public synchronized T remove() {
        T head = poll();
        if (head == null) {
            throw new IllegalStateException("Queue is empty");
        }
        return head;
    }

    @Override
    public synchronized T poll() {
        if (deque.isEmpty()) {
            return null;
        }
        return removeAndNotify();
    }

    @Override
    public synchronized T take() throws InterruptedException {
        while (deque.isEmpty()) {
            wait();
        }
        return removeAndNotify();
    }

    @Override
    public synchronized T poll(long timeout, TimeUnit unit) throws InterruptedException {
        int enters = 0;
        synchronized (this) {
            while (deque.isEmpty()) {
                if (enters++ > 0) {
                    return null;
                }
                wait(unit.toMillis(timeout));
            }
            return removeAndNotify();
        }
    }

    private T removeAndNotify() {
        T head = deque.remove();
        notifyAll();
        return head;
    }

    @Override
    public synchronized boolean remove(Object o) {
        boolean removed = deque.remove(o);
        if (removed) {
            notifyAll();
        }
        return removed;
    }

    @Override
    public synchronized int drainTo(Collection<? super T> c) {
        if (c == this) {
            throw new IllegalArgumentException("Unable drain to itself");
        }
        int transferred = deque.size();
        c.addAll(deque);
        deque.clear();

        notifyAll();
        return transferred;
    }

    @Override
    public synchronized int drainTo(Collection<? super T> c, int maxElements) {
        if (c == this) {
            throw new IllegalArgumentException("Unable drain to itself");
        }
        if (maxElements <= 0) {
            return 0;
        }
        int transferred = Math.min(maxElements, deque.size());
        for (int i = 0; i < transferred; i++) {
            c.add(deque.remove());
        }

        notifyAll();
        return transferred;
    }

    @Override
    public synchronized int remainingCapacity() {
        return limit - deque.size();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return deque.contains(o);
    }

    @Override
    public synchronized T element() {
        return deque.element();
    }

    @Override
    public synchronized T peek() {
        return deque.peek();
    }

    @Override
    public synchronized int size() {
        return deque.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return new ArrayList<T>(deque).iterator();
    }

    @Override
    public synchronized Object[] toArray() {
        return deque.toArray();
    }

    @Override
    public synchronized <T> T[] toArray(T[] a) {
        return deque.toArray(a);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return deque.containsAll(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {
        boolean added = deque.addAll(c);
        if (added) {
            notifyAll();
        }
        return added;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        boolean removed = deque.removeAll(c);
        if (removed) {
            notifyAll();
        }
        return removed;
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        boolean retained = deque.retainAll(c);
        if (retained) {
            notifyAll();
        }
        return retained;
    }

    @Override
    public synchronized void clear() {
        deque.clear();
        notifyAll();
    }
}

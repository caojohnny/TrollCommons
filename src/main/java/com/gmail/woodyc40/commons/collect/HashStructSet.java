package com.gmail.woodyc40.commons.collect;

import java.util.*;

public class HashStructSet<E> implements Set<E> {
    private final AbstractHashStruct<E, Object> delegate;
    private final Object value = new Object();

    public HashStructSet() {
        this(16);
    }

    public HashStructSet(final int size) {
        this.delegate = new AbstractHashStruct<E, Object>() {
            @Override protected AbstractHashStruct.Node[] buckets() {
                return new AbstractHashStruct.Node[size];
            }
        };
    }

    public HashStructSet(Collection<? extends E> master) {
        this(master.size());
        this.addAll(master);
    }

    @Override public int size() {
        return this.delegate.getSize();
    }

    @Override public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override public boolean contains(Object o) {
        return this.delegate.containsKey((E) o);
    }

    @Override public Iterator<E> iterator() {
        return this.delegate.keyIterator();
    }

    @Override public Object[] toArray() {
        Object[] objects = new Object[this.size()];
        int index = 0;

        for (AbstractHashStruct<E, Object>.Node bucket : this.delegate.getBuckets()) {
            for (AbstractHashStruct<E, Object>.Node last = bucket; last != null; last = last.getNext())
                objects[index] = last.getKey();
        }
        return objects;
    }

    @Override public <T> T[] toArray(T[] ts) {
        T[] ts1 = Arrays.copyOf(ts, ts.length + this.size());
        System.arraycopy(this.toArray(), 0, ts1, ts1.length, ts.length - 1);
        return ts1;
    }

    @Override public boolean add(E e) {
        return this.delegate.put(e, this.value) == null;
    }

    @Override public boolean remove(Object o) {
        return this.delegate.remove((E) o) == null;
    }

    @Override public boolean containsAll(Collection<?> objects) {
        for (Object o : objects) {
            if (!this.contains(o)) return false;
        }

        return true;
    }

    @Override public boolean addAll(Collection<? extends E> es) {
        for (E o : es)
            if (!this.add(o)) return false;

        return true;
    }

    @Override public boolean retainAll(Collection<?> objects) {
        for (AbstractHashStruct<E, Object>.Node bucket : this.delegate.getBuckets()) {
            for (AbstractHashStruct<E, Object>.Node last = bucket; last != null; last = last.getNext())
                if (!objects.contains(last.getKey()))
                    if (!this.remove(last.getKey())) return false;
        }

        return true;
    }

    @Override public boolean removeAll(Collection<?> objects) {
        for (AbstractHashStruct<E, Object>.Node bucket : this.delegate.getBuckets()) {
            for (AbstractHashStruct<E, Object>.Node last = bucket; last != null; last = last.getNext())
                if (objects.contains(last.getKey()))
                    if (!this.remove(last.getKey())) return false;
        }

        return true;
    }

    @Override public void clear() {
        this.delegate.clear();
    }
}

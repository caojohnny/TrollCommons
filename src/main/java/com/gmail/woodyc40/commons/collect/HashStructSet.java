/*
 * Copyright 2014 AgentTroll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmail.woodyc40.commons.collect;

import java.util.*;

/**
 * Implementation of {@link java.util.Set} using {@link com.gmail.woodyc40.commons.collect.AbstractHashStruct}
 *
 * @param <E> the type of item to store
 * @author AgentTroll
 * @version 1.0
 * @see java.util.Set
 * @see java.util.Collection
 */
public class HashStructSet<E> implements Set<E> {
    /** The delegate to perform actions on */
    private final AbstractHashStruct<E, Object> delegate;
    /** The value stored in the struct, just a placeholder */
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
        Object[] ts1 = Arrays.copyOf(ts, ts.length + this.size());
        System.arraycopy(this.toArray(), 0, ts1, ts1.length, ts.length - 1);
        return (T[]) ts1;
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

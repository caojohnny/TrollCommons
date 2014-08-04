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

import lombok.Getter;

import java.util.*;

/**
 * Implementation of {@link java.util.Map} using {@link com.gmail.woodyc40.commons.collect.AbstractHashStruct}
 *
 * @param <K> the type of key to use
 * @param <V> the type of value to use
 * @author AgentTroll
 * @version 1.0
 * @see java.util.Map
 */
public class HashStructMap<K, V> implements Map<K, V> {
    /** The delegating hash structure to perform base ops on */
    @Getter private final AbstractHashStruct<K, V> delegate;

    /**
     * Create a new map based on {@link com.gmail.woodyc40.commons.collect.HashStructMap} with starting size of 16
     */
    public HashStructMap() {
        this(16);
    }

    /**
     * Create a map based on {@link com.gmail.woodyc40.commons.collect.HashStructMap} with specified size
     *
     * @param size initial map size to use
     */
    public HashStructMap(final int size) {
        this.delegate = new AbstractHashStruct<K, V>() {
            @Override protected AbstractHashStruct.Node[] buckets() {
                return new AbstractHashStruct.Node[size];
            }
        };
    }

    /**
     * Create a map based on {@link com.gmail.woodyc40.commons.collect.HashStructMap} with initial entries
     *
     * @param master the parent map to copy the entries over
     */
    public HashStructMap(Map<? extends K, ? extends V> master) {
        this(master.size());
        this.putAll(master);
    }

    /**
     * Create a map from a base struct map
     *
     * @param hashStruct the struct to use as a delegate
     */
    public HashStructMap(AbstractHashStruct<K, V> hashStruct) {
        this.delegate = hashStruct;
    }

    @Override public int size() {
        return this.delegate.getSize();
    }

    @Override public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override public boolean containsKey(Object o) {
        return this.delegate.containsKey((K) o); // Let it throw ClassCastException if it has to
    }

    @Override public boolean containsValue(Object o) {
        return this.delegate.containsValue((V) o);
    }

    @Override public V get(Object o) {
        return this.delegate.get((K) o);
    }

    @Override public V put(K k, V v) {
        return this.delegate.put(k, v);
    }

    @Override public V remove(Object o) {
        return this.delegate.remove((K) o);
    }

    @Override public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
            this.delegate.put(entry.getKey(), entry.getValue());
    }

    @Override public void clear() {
        this.delegate.clear();
    }

    @Override public Set<K> keySet() {
        Set<K> keys = new HashStructSet<>();
        for (AbstractHashStruct<K, V>.Node bucket : this.delegate.getBuckets()) {
            for (AbstractHashStruct<K, V>.Node last = bucket; last != null; last = last.getNext())
                keys.add(last.getKey());
        }
        return keys;
    }

    @Override public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        for (AbstractHashStruct<K, V>.Node bucket : this.delegate.getBuckets()) {
            for (AbstractHashStruct<K, V>.Node last = bucket; last != null; last = last.getNext())
                values.add(last.getValue());
        }
        return values;
    }

    @Override public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> keys = new HashStructSet<>();
        for (AbstractHashStruct<K, V>.Node bucket : this.delegate.getBuckets()) {
            for (AbstractHashStruct<K, V>.Node last = bucket; last != null; last = last.getNext())
                keys.add(new AbstractMap.SimpleEntry<>(last.getKey(), last.getValue()));
        }
        return keys;
    }
}

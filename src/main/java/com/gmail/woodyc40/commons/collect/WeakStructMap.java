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

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Weak structure mapping with configurable weak values, keys, or both. Uses {@link java.lang.ref.WeakReference} to
 * manage entries in the struct
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.1
 */
public class WeakStructMap<K, V> implements Map<K, V> {
    /** Delegation instance to move operations to */
    private final WeakStructMap.InternalDelegate<K, V> delegate;

    /**
     * Creates a new WeakReference based collection
     *
     * @param config the configuration to use weak references in
     */
    public WeakStructMap(WeakStructMap.WeakConfiguration config) {
        switch (config) {
            case KEYS:
                this.delegate = new WeakStructMap.WeakKeyMap<>();
                break;
            case VALUES:
                this.delegate = new WeakValMap<>();
                break;
            case BOTH:
                this.delegate = new WeakMap<>();
                break;
            default:
                this.delegate = new WeakMap<>();
        }
    }

    @Override public int size() {
        return this.delegate.getDelegate().size();
    }

    @Override public boolean isEmpty() {
        return this.delegate.getDelegate().isEmpty();
    }

    @Override public boolean containsKey(Object o) {
        return this.delegate.containsKey(o); // Let it throw ClassCastException if it has to
    }

    @Override public boolean containsValue(Object o) {
        return this.delegate.containsValue(o);
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
        this.delegate.putAll(map);
    }

    @Override public void clear() {
        this.delegate.getDelegate().clear();
    }

    @Override public Set<K> keySet() {
        return this.delegate.keySet();
    }

    @Override public Collection<V> values() {
        return this.delegate.values();
    }

    @Override public Set<Map.Entry<K, V>> entrySet() {
        return this.delegate.entrySet();
    }

    /**
     * Configuration values to decide which parts of the collection are weak and strong references
     *
     * @author AgentTroll
     * @version 1.0
     * @since 1.1
     */
    public enum WeakConfiguration {
        /** Weak keys configuration */
        KEYS,
        /** Weak values configuration */
        VALUES,
        /** Weak keys and values configuration */
        BOTH
    }

    /**
     * Delegation container for the weak structure
     *
     * @param <K> the key type
     * @param <V> the value type
     * @author AgentTroll
     * @version 1.0
     * @since 1.1
     */
    private interface InternalDelegate<K, V> {
        boolean containsKey(Object o);

        boolean containsValue(Object o);

        V get(Object o);

        V put(K k, V v);

        V remove(Object o);

        void putAll(Map<? extends K, ? extends V> map);

        Set<K> keySet();

        Collection<V> values();

        Set<Map.Entry<K, V>> entrySet();

        Map<?, ?> getDelegate();
    }

    /**
     * Delegation implementation for weak keys
     *
     * @param <K> the key type
     * @param <V> the value type
     * @author AgentTroll
     * @version 1.0
     * @since 1.1
     */
    private static class WeakKeyMap<K, V> implements WeakStructMap.InternalDelegate<K, V> {
        @Getter private final Map<WeakReference<K>, V> delegate = new StructBuilder().buildMap();

        @Override public boolean containsKey(Object o) {
            return this.delegate.containsKey(new WeakReference<>((K) o));
        }

        @Override public boolean containsValue(Object o) {
            return this.delegate.containsValue(o);
        }

        @Override public V get(Object o) {
            return this.delegate.get(new WeakReference<>((K) o));
        }

        @Override public V put(K k, V v) {
            return this.delegate.put(new WeakReference<>(k), v);
        }

        @Override public V remove(Object o) {
            return this.delegate.remove(new WeakReference<>((K) o));
        }

        @Override public void putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
                this.put(entry.getKey(), entry.getValue());
        }

        @Override public Set<K> keySet() {
            Set<K> set = new HashStructSet<>();
            for (Map.Entry<WeakReference<K>, V> entry : this.delegate.entrySet()) set.add(entry.getKey().get());
            return set;
        }

        @Override public Collection<V> values() {
            Collection<V> collection = new ArrayList<>();
            for (Map.Entry<WeakReference<K>, V> entry : this.delegate.entrySet()) collection.add(entry.getValue());
            return collection;
        }

        @Override public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set = new HashStructSet<>();
            for (Map.Entry<WeakReference<K>, V> entry : this.delegate.entrySet())
                set.add(new AbstractMap.SimpleEntry<>(entry.getKey().get(), entry.getValue()));
            return set;
        }
    }

    /**
     * Delegation implementation for weak values
     *
     * @param <K> the key type
     * @param <V> the value type
     * @author AgentTroll
     * @version 1.0
     * @since 1.1
     */
    private class WeakValMap<K, V> implements WeakStructMap.InternalDelegate<K, V> {
        @Getter private final Map<K, WeakReference<V>> delegate = new StructBuilder().buildMap();

        @Override public boolean containsKey(Object o) {
            return this.delegate.containsKey(o);
        }

        @Override public boolean containsValue(Object o) {
            return this.delegate.containsValue(new WeakReference<>((V) o));
        }

        @Override public V get(Object o) {
            return this.delegate.get(o).get();
        }

        @Override public V put(K k, V v) {
            return this.delegate.put(k, new WeakReference<>(v)).get();
        }

        @Override public V remove(Object o) {
            return this.delegate.remove(o).get();
        }

        @Override public void putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
                this.put(entry.getKey(), entry.getValue());
        }

        @Override public Set<K> keySet() {
            Set<K> set = new HashStructSet<>();
            for (Map.Entry<K, WeakReference<V>> entry : this.delegate.entrySet()) set.add(entry.getKey());
            return set;
        }

        @Override public Collection<V> values() {
            Collection<V> collection = new ArrayList<>();
            for (Map.Entry<K, WeakReference<V>> entry : this.delegate.entrySet())
                collection.add(entry.getValue().get());
            return collection;
        }

        @Override public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set = new HashStructSet<>();
            for (Map.Entry<K, WeakReference<V>> entry : this.delegate.entrySet())
                set.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().get()));
            return set;
        }
    }

    /**
     * Delegation implementation for weak keys and values
     *
     * @param <K> the key type
     * @param <V> the value type
     * @author AgentTroll
     * @version 1.0
     * @since 1.1
     */
    private class WeakMap<K, V> implements WeakStructMap.InternalDelegate<K, V> {
        @Getter private final Map<WeakReference<K>, WeakReference<V>> delegate = new StructBuilder().buildMap();

        @Override public boolean containsKey(Object o) {
            return this.delegate.containsKey(new WeakReference<>((K) o));
        }

        @Override public boolean containsValue(Object o) {
            return this.delegate.containsValue(new WeakReference<>((V) o));
        }

        @Override public V get(Object o) {
            return this.delegate.get(new WeakReference<>((K) o)).get();
        }

        @Override public V put(K k, V v) {
            return this.delegate.put(new WeakReference<>(k), new WeakReference<>(v)).get();
        }

        @Override public V remove(Object o) {
            return this.delegate.remove(new WeakReference<>((K) o)).get();
        }

        @Override public void putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
                this.put(entry.getKey(), entry.getValue());
        }

        @Override public Set<K> keySet() {
            Set<K> set = new HashStructSet<>();
            for (Map.Entry<WeakReference<K>, WeakReference<V>> entry : this.delegate.entrySet())
                set.add(entry.getKey().get());
            return set;
        }

        @Override public Collection<V> values() {
            Collection<V> collection = new ArrayList<>();
            for (Map.Entry<WeakReference<K>, WeakReference<V>> entry : this.delegate.entrySet())
                collection.add(entry.getValue().get());
            return collection;
        }

        @Override public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set = new HashStructSet<>();
            for (Map.Entry<WeakReference<K>, WeakReference<V>> entry : this.delegate.entrySet())
                set.add(new AbstractMap.SimpleEntry<>(entry.getKey().get(), entry.getValue().get()));
            return set;
        }
    }
}
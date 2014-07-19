package com.gmail.woodyc40.commons.collect;

import lombok.Getter;

import java.util.*;

public class HashStructMap<K, V> implements Map<K, V> {
    @Getter private final AbstractHashStruct<K, V> delegate;

    public HashStructMap() {
        this(16);
    }

    public HashStructMap(final int size) {
        this.delegate = new AbstractHashStruct<K, V>() {
            @Override protected AbstractHashStruct.Node[] buckets() {
                return new AbstractHashStruct.Node[size];
            }
        };    
    }
    
    public HashStructMap(Map<? extends K, ? extends V> master) {
        this(master.size());
        this.putAll(master);
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

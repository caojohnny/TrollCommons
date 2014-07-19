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

import com.gmail.woodyc40.commons.misc.MultiParamRunnable;
import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.*;

import java.util.Iterator;
import java.util.Map;

public abstract class AbstractHashStruct<K, V> {
    @Getter private AbstractHashStruct<K, V>.Node[] buckets = this.buckets();

    @Getter private int size;
    private int resizeThresh = 14;

    @Getter @Setter private HashStrategy stategy = HashStrategy.A_TROLL;

    //===============[ Hooks ] ===============//

    protected abstract AbstractHashStruct.Node[] buckets();

    //===============[ Map methods ]===============//

    public V put(K k, V v) {
        int hash = this.posOf(k);
        AbstractHashStruct<K, V>.Node toSet = this.search(k, hash);
        V old = toSet.getValue();

        toSet.setBucket(hash);
        toSet.setValue(v);
        return old;
    }

    public V get(K k) {
        int hash = this.posOf(k);
        AbstractHashStruct<K, V>.Node node = this.loop(k, this.buckets[hash]);
        if (node == null) return null;
        else return node.getValue();
    }

    public V remove(K k) {
        AbstractHashStruct<K, V>.Node head = this.buckets[this.posOf(k)];
        if (head == null) return null;
        V old = head.getValue();

        this.remove(k, head);
        return old;
    }

    public void clear() {
        this.buckets = new AbstractHashStruct.Node[16];
        this.size = 0;
        this.resizeThresh = 14;
    }

    public boolean containsKey(K ky) {
        K k;
        return (k = this.loop(ky, this.buckets[this.posOf(ky)]).getKey()) != null && k.equals(ky);
    }

    public boolean containsValue(V v) {
        for (AbstractHashStruct<K, V>.Node node : this.buckets)
            if (node.getValue().equals(v))
                return true;
        return false;
    }
    
    public Iterator<K> keyIterator() {
        return new KeyIterator();
    }

    public Iterator<V> valueIterator() {
        return new ValueIterator();
    }

    //===============[ Util methods ]===============//

    private int posOf(K key) {
        return this.stategy.hash(key, this.buckets.length);
    }

    private void checkSize() {
        if (this.size == this.resizeThresh) {
            int newLen = this.resizeThresh *= 2;

            AbstractHashStruct<K, V>.Node[] resize = new AbstractHashStruct.Node[newLen];
            System.arraycopy(this.buckets, 0, resize, 0, this.size);
            this.buckets = resize;
        }
    }

    private AbstractHashStruct<K, V>.Node loop(K k, AbstractHashStruct<K, V>.Node head) {
        AbstractHashStruct<K, V>.Node tail = head;
        while (tail != null) {
            if (tail.getKey().equals(k))
                return tail;
            tail = tail.getNext();
        }

        return null;
    }

    private AbstractHashStruct<K, V>.Node search(K k, int hash) {
        // Implementation:
        // 4 Step process
        // Step 1: Loop through all linked nodes
        // Step 2: Check for node key equality to provided
        // Step 3: If it is equal, return it
        // Step 4: If none found, return the result of create(...)

        AbstractHashStruct<K, V>.Node head = this.buckets[hash];
        AbstractHashStruct<K, V>.Node tail = this.loop(k, head);
        if (tail != null) return tail;
        else return this.create(k, null, hash);
    }

    private AbstractHashStruct<K, V>.Node create(K k, AbstractHashStruct<K, V>.Node previous, int hash) {
        AbstractHashStruct<K, V>.Node node = new AbstractHashStruct<K, V>.Node(k, null, 0, null);
        if (previous == null) {
            this.buckets[hash] = node;
            this.size += 1;
            this.checkSize();
            return node;
        }

        previous.setNext(node);
        this.size += 1;
        this.checkSize();

        return node;
    }

    private void remove(K k, AbstractHashStruct<K, V>.Node head) {
        AbstractHashStruct<K, V>.Node leading = head;
        AbstractHashStruct<K, V>.Node tail = leading == null ? null : leading.getNext();

        while (tail != null) {
            if (tail.getKey().equals(k)) {
                leading.setNext(tail.getNext());
                this.size -= 1;
                return;
            }

            tail = tail.getNext();
            leading = leading.getNext();
        }

        // If it reaches here, it can mean that
        // leading != null && trailing == null or
        // leading == null && trailing == null

        if (leading != null && leading.getKey().equals(k))
            this.buckets[leading.getBucket()] = null;

        this.size -= 1;
    }

    //===============[ Sub-classes ] ===============//

    @Getter @AllArgsConstructor @ToString @EqualsAndHashCode
    public class Node implements Map.Entry<K, V> {
        private final   K                             key;
        private         V                             value;
        @Setter private int                           bucket;
        @Setter private AbstractHashStruct<K, V>.Node next;

        @Override public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    public class KeyIterator extends AbstractIterator<K> {
        @Override protected Object[] allVals() {
            int index = 0;
            Object[] vals = new Object[AbstractHashStruct.this.getSize()];
            for (AbstractHashStruct<K, V>.Node bucket : AbstractHashStruct.this.getBuckets()) {
                for (AbstractHashStruct<K, V>.Node last = bucket; last != null; last = last.getNext())
                    vals[index] = last.getKey();
                index++;
            }
            return vals;
        }

        @Override protected void removeValue(K val) {
            AbstractHashStruct.this.remove(val);
        }
    }

    public class ValueIterator extends AbstractIterator<V> {
        @Override protected Object[] allVals() {
            int index = 0;
            Object[] vals = new Object[AbstractHashStruct.this.getSize()];
            for (AbstractHashStruct<K, V>.Node bucket : AbstractHashStruct.this.getBuckets()) {
                for (AbstractHashStruct<K, V>.Node last = bucket; last != null; last = last.getNext())
                    vals[index] = last.getValue();
                index++;
            }
            return vals;
        }

        @Override protected void removeValue(V val) {
        }
    }

    @AllArgsConstructor
    public enum HashStrategy {
        MURMUR(new MultiParamRunnable<Integer, Object, Integer>() {
            @Override public Integer run(Object o, Integer i) {
                // Implementation:
                // 3 step process -
                // Step 1: Hash the current hashCode of the key
                // Step 2: The value may be negative, convert to unsigned long
                // Step 3: Find the remainder by dividing by the amount of buckets
                // The value of Step 3 is returned as the bucket index

                int hash = HashStrategy.hasher.hashInt(o.hashCode()).asInt();
                long convert = UnsafeProvider.normalize(hash);

                return (int) (convert % (long) i);
            }
        }),

        JAVA(new MultiParamRunnable<Integer, Object, Integer>() {
            @Override public Integer run(Object o, Integer i) {
                int h = o.hashCode();
                h ^= (h >>> 20) ^ (h >>> 12);
                int bit = h ^ (h >>> 7) ^ (h >>> 4);

                return bit & (i - 1);
            }
        }),

        A_TROLL(new MultiParamRunnable<Integer, Object, Integer>() {
            @Override public Integer run(Object o, Integer i) {
                int h = o.hashCode();
                h |= 769 ^ h ^ (20 >> 3 << h);
                h ^= 6151 ^ (h >>> 4) ^ (h ^ 4);

                long unsign = UnsafeProvider.normalize(h);

                return (int) (unsign % (long) i);
            }
        });

        private static final HashFunction hasher = Hashing.murmur3_32();

        MultiParamRunnable<Integer, Object, Integer> hashFunc;

        public int hash(Object key, int len) {
            return this.hashFunc.run(key, len);
        }
    }
}

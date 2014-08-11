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

import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.*;

import java.util.Iterator;
import java.util.Map;

/**
 * This is a base utility used to build hashing data structures similar to that of {@link java.util.HashMap} and {@link
 * java.util.HashSet}.
 * <p>
 * <p>By default, this will have a resizing threshold of 14 items, which can be changed using {@link
 * #setResizeThresh(int)} and a hashing strategy defaulted to {@link com.gmail.woodyc40.commons.collect
 * .AbstractHashStruct.HashStrategy#A_TROLL}
 * that can be changed using {@link #setStrategy(com.gmail.woodyc40.commons.collect.AbstractHashStruct.HashStrategy)}
 * </p>
 * <p>
 * <p>Benchmarks for all 3 hash strategies can be found <a href="https://github
 * .com/AgentTroll/BukkitCommons/blob/master/src/test/com/gmail/woodyc40/commons/HashBenchmark.java>here</a></p>
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@Getter
public abstract class AbstractHashStruct<K, V> {
    /** The entry storage */
    private AbstractHashStruct<K, V>.Node[] buckets = this.buckets();

    /** Amount of entries inserted */
    private int size;
    /** The threshold until the map resizes */
    @Setter private int resizeThresh = 14;

    /** The strategy employed to hash the keys on insertion */
    @Setter private AbstractHashStruct.HashStrategy strategy = this.hashStrategy();

    //===============[ Hook ] ===============//

    /**
     * Provider of the bucket array of nodes
     *
     * @return the initial value of the node array, including size
     */
    protected abstract AbstractHashStruct.Node[] buckets();

    protected AbstractHashStruct.HashStrategy hashStrategy() {
        return AbstractHashStruct.HashStrategy.A_TROLL;
    }

    //===============[ Structure methods ]===============//

    /**
     * Insertion operation on the structure
     *
     * @param k the key
     * @param v the value
     * @return the old value, or null if there is none
     */
    public final V put(K k, V v) {
        int hash = this.posOf(k);
        AbstractHashStruct<K, V>.Node toSet = this.search(k, hash);
        V old = toSet.getValue();

        toSet.setBucket(hash);
        toSet.setValue(v);
        return old;
    }

    /**
     * Retrieves the value associated with the key provided
     *
     * @param k the key to get the value from
     * @return the value associated with the key upon insertion, null if the key is not found, or the key is associated
     * with a null value
     */
    public final V get(K k) {
        int hash = this.posOf(k);
        AbstractHashStruct<K, V>.Node node = this.loop(k, this.buckets[hash]);
        if (node == null) return null;
        else return node.getValue();
    }

    /**
     * Removes an entry from the bucket array
     *
     * @param k the key which a value is associated to that will be removed from the map
     * @return the old value associated with the key removed, null if it is not actually removed, the key is not found,
     * or the value associated with the key is null
     */
    public final V remove(K k) {
        AbstractHashStruct<K, V>.Node head = this.buckets[this.posOf(k)];
        if (head == null) return null;
        V old = head.getValue();

        this.remove(k, head);
        return old;
    }

    /**
     * Removes all entries from the bucket array, setting the size to zero, along with the resizing threshold to 14.
     */
    public final void clear() {
        this.buckets = new AbstractHashStruct.Node[16];
        this.size = 0;
        this.resizeThresh = 14;
    }

    /**
     * Asks the bucket array if one of the entries has a key provided
     *
     * @param ky the key to check for in the bucket array
     * @return {@code false} if the key is not found in the buckets, {@code true} if found
     */
    public final boolean containsKey(K ky) {
        K k;
        return (k = this.loop(ky, this.buckets[this.posOf(ky)]).getKey()) != null && k.equals(ky);
    }

    /**
     * Asks the bucket array if one of the entries holds the value provided
     * <p>
     * <p>Note that this is an O(n) operation</p>
     *
     * @param v the value to check for in the bucket array
     * @return {@code false} if the value is not found in the buckets, {@code true} if found
     */
    public final boolean containsValue(V v) {
        for (AbstractHashStruct<K, V>.Node node : this.buckets)
            if (node.getValue().equals(v))
                return true;
        return false;
    }

    /**
     * The iterator over all of the keys in this structure
     *
     * @return the iteration structure that provides access to all keys
     */
    public final Iterator<K> keyIterator() {
        return new KeyIterator();
    }

    /**
     * The iterator over all of the values in this structure
     *
     * @return the iteration structure that provides access to all values
     */
    public final Iterator<V> valueIterator() {
        return new ValueIterator();
    }

    //===============[ Util methods ]===============//

    /**
     * Returns the position of the key via hashing and finding the hashed bucket
     *
     * @param key the key to use in hashing
     * @return the position in the bucket array
     */
    private int posOf(K key) {
        return this.strategy.hash(key, this.buckets.length);
    }

    /**
     * Resizes the bucket array if necessary
     */
    private void checkSize() {
        if (this.size == this.resizeThresh) {
            int newLen = this.resizeThresh *= 2;

            AbstractHashStruct<K, V>.Node[] resize = new AbstractHashStruct.Node[newLen];
            System.arraycopy(this.buckets, 0, resize, 0, this.size);
            this.buckets = resize;
        }
    }

    /**
     * Iterates through the linked list bucket index for the node containing the key
     *
     * @param k    the key to get the node from
     * @param head the first node in the bucket
     * @return the node that holds the key, null if not found
     */
    private AbstractHashStruct<K, V>.Node loop(K k, AbstractHashStruct<K, V>.Node head) {
        AbstractHashStruct<K, V>.Node tail = head;
        while (tail != null) {
            if (tail.getKey().equals(k))
                return tail;
            tail = tail.getNext();
        }

        return null;
    }

    /**
     * Performs a {@link #loop(Object, com.gmail.woodyc40.commons.collect.AbstractHashStruct.Node)}, returning the node
     * if non-{@code null}, or creating a key if {@code null}
     *
     * @param k    the key to get the node from
     * @param hash the position of the key
     * @return the node associated with the key in the bucket array
     */
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

    /**
     * Creates a node and links it with the previous, or sets the bucket position to the node if the first in the
     * particular bucket
     *
     * @param k        the key to create the node with
     * @param previous the previous node, can be {@code null}
     * @param hash     the position to place the node in the bucket array
     * @return the node created
     */
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

    /**
     * Removes a key from a bucket
     *
     * @param k    the key in the entry which will be removed
     * @param head the first node in the bucket of the to-be removed entry
     */
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

    /**
     * An enumeration of strategies for finding the position of an object key on a hashtable
     *
     * @author AgentTroll
     * @version 1.0
     * @see com.google.common.hash.Murmur3_32HashFunction
     */
    public enum HashStrategy {
        /**
         * Uses {@link com.google.common.hash.Murmur3_32HashFunction} to hash the values, and mod the length of buckets
         */
        MURMUR {
            @Override public int hash(Object o, int i) {
                // Implementation:
                // 3 step process -
                // Step 1: Hash the current hashCode of the key
                // Step 2: The value may be negative, convert to unsigned long
                // Step 3: Find the remainder by dividing by the amount of buckets
                // The value of Step 3 is returned as the bucket index

                int hash = AbstractHashStruct.HashStrategy.hasher.hashInt(o.hashCode()).asInt();
                long convert = UnsafeProvider.normalize(hash);

                return (int) (convert % (long) i);
            }
        },

        /**
         * The default {@link java.util.HashMap} hashing strategy, from OpenJDK
         */
        JAVA {
            @Override public int hash(Object o, int i) {
                int h = o.hashCode();
                h ^= h >>> 20 ^ h >>> 12;
                int bit = h ^ h >>> 7 ^ h >>> 4;

                return bit & i - 1;
            }
        },

        /**
         * Custom hashing function that is faster than MURMUR and better distribution than JAVA
         */
        A_TROLL {
            @Override public int hash(Object o, int i) {
                long h = UnsafeProvider.normalize(o.hashCode());
                h = (h >> 16 ^ h) * 0x301L;
                h = (h >> 16 ^ h) * 0x301L;
                h = h >> 16 ^ h;

                return (int) (h % (long) i);
            }
        };

        /** The hasher to use in {@link com.gmail.woodyc40.commons.collect.AbstractHashStruct.HashStrategy#MURMUR} */
        private static final HashFunction hasher = Hashing.murmur3_32();

        /**
         * Hash the key for the length of the bucket array
         *
         * @param key the key to find the position of
         * @param len the number of bucket positions available
         * @return the index to put the key
         */
        public abstract int hash(Object key, int len);
    }

    /**
     * The linked list structure that holds a key, value, position, and next key in the list
     *
     * @author AgentTroll
     * @version 1.0
     * @see java.util.Map.Entry
     */
    @Getter @AllArgsConstructor @ToString @EqualsAndHashCode
    public class Node implements Map.Entry<K, V> {
        /** The key */
        private final   K                             key;
        /** The value */
        private         V                             value;
        /** The position in the bucket array */
        @Setter private int                           bucket;
        /** The next node in the linked-list */
        @Setter private AbstractHashStruct<K, V>.Node next;

        @Override public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    /**
     * The iterator which goes over all the keys represented by this structure
     *
     * @author AgentTroll
     * @version 1.0
     * @see java.util.Iterator
     * @see com.gmail.woodyc40.commons.collect.AbstractIterator
     */
    protected class KeyIterator extends AbstractIterator<K> {
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

    /**
     * The iterator that goes over all of the values in this structure
     *
     * @author AgentTroll
     * @version 1.0
     * @see java.util.Iterator
     * @see com.gmail.woodyc40.commons.collect.AbstractIterator
     */
    protected class ValueIterator extends AbstractIterator<V> {
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

        /**
         * {@inheritDoc}
         * <p>
         * <p>DOES NOT SUPPORT REMOVAL</p>
         */
        @Override protected void removeValue(V val) {
            throw new UnsupportedOperationException("You should remove with a key iterator instead");
        }
    }
}

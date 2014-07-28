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

package com.gmail.woodyc40.commons;

import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.util.*;

public class BashMap {
    private static final AbstractHashStruct hashStruct = new AbstractHashStruct() {
        @Override protected Node[] buckets() {
            return new Node[16];
        }
    };

    public static void main(String[] args) {
        while (true) {
            hashStruct.put(new Object(), new Object());
        }
    }

    public static final class UnsafeProvider {
        /**
         * The Unsafe instance.
         */
        private static final Unsafe PROVIDER = BashMap.UnsafeProvider.initUnsafe();

        /**
         * Should not be instantiated, hence the private constructor.
         */
        private UnsafeProvider() {}

        /**
         * Used to acquire the instance of {@link Unsafe}.
         * <p/>
         * <p/>
         * This is more of a "hack" than a direct method, as it uses reflection in order to obtain the stored 
         * instance of
         * the Unsafe singleton. Unsafe cannot be accessed through its singleton {@link Unsafe#getUnsafe()} method, 
         * as it
         * checks the calling {@code class} to make sure that the {@link ClassLoader} is {@code null}. Java {@code class}es
         * don't use a {@link ClassLoader}, hence the reason why they can call {@link Unsafe#getUnsafe()} without {@link
         * SecurityException}.
         * <p/>
         * <p/>
         * Being a "hack", this method will never be guaranteed to actually get an instance of Unsafe, the field that stores
         * the instance of Unsafe <strong>is named differently on different implementations of the JVM</strong>. This
         * specific method is designed to get Unsafe from the Java HotSpot JVM.
         *
         * @return the Unsafe instance acquired from the HotSpot JVM Unsafe {@code class}
         */
        private static Unsafe initUnsafe() {
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                return (Unsafe) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException x) {
                x.printStackTrace();
            }
            return null;
        }

        /**
         * Acquires the raw instance Unsafe "hacked" from the source.
         * <p/>
         * <p/>
         * If an exception was thrown when this class was loaded, this method will most likely {@code return null}. JVM
         * implementations may modify the field in which the instance of Unsafe is stored, throwing an exception when normal
         * acquisition methods are applied.
         * <p/>
         * <p/>
         * This is guaranteed to <strong>not</strong> {@code return null} on the HotSpot JVM
         *
         * @return the Unsafe instance received when this {@code class} was loaded.
         * @throws IllegalStateException if the Unsafe instance was not acquired already during load
         */
        public static Unsafe provide() {
            if (BashMap.UnsafeProvider.PROVIDER == null)
                throw new IllegalStateException("Cannot be used, Unsafe is null");

            return BashMap.UnsafeProvider.PROVIDER;
        }

        // Reflection area

        /**
         * Util method to get whether or not a field is {@code static}.
         *
         * @param field the field to check for a {@code static} modifier
         * @return {@code true} to indicate that a field is {@code static}, {@code false} if the field does not have a
         * {@code static} modifier
         */
        private static boolean fieldStatic(Member field) {
            return Modifier.isStatic(field.getModifiers());
        }

        /**
         * Provides field offset calculations of a field in order to find its location in a {@code class}.
         * <p/>
         * <p/>
         * The field parameter should never be {@code null}. An exception will be thrown if it is.
         *
         * @param field the field to {@code return} the offset of
         * @return the relative {@code class} location offset of the field
         * @throws IllegalArgumentException if the field parameter is {@code null}
         */
        private static long fieldOffset(Field field) {
            if (BashMap.UnsafeProvider.fieldStatic(field))
                return BashMap.UnsafeProvider.PROVIDER.staticFieldOffset(field);
            return BashMap.UnsafeProvider.PROVIDER.objectFieldOffset(field);
        }

        /**
         * Acquires the raw field value of the specified field of a {@code class}.
         * <p/>
         * <p/>
         * It may {@code return null}. This can happen under the following circumstances: <ul>
         * <p/>
         * <li>If the field value is {@code null} for the particular instance (More common).</li> <li>If one of the
         * parameters required to be non-{@code null} is {@code null}</li> <li>Or if the field is not found or not able to
         * be accessed. Check method call, spelling, and class source. Indicated by a thrown exception.</li>
         * <p/>
         * </ul>
         * <p/>
         * <p/>
         * The holder parameter may be null for static fields. In fact, it is set null for this purpose in the method
         * itself, so it will not matter nevertheless.
         *
         * @param holder the instance of the object to acquire the value from.
         * @return the value of the acquired field from the instance of the holder parameter
         * @throws IllegalArgumentException if a nullable parameter is {@code null}
         */
        public static Object acquireField(Field field, Object holder) {
            long offset = BashMap.UnsafeProvider.fieldOffset(field);
            if (BashMap.UnsafeProvider.fieldStatic(field))
                return BashMap.UnsafeProvider.PROVIDER.getObject(field.getDeclaringClass(), offset);

            return BashMap.UnsafeProvider.PROVIDER.getObject(holder, offset);
        }

        /**
         * <p/>
         * This method will fail under the following circumstances: <ul>
         * <p/>
         * <li>If one of the parameters required to be non-{@code null} is {@code null}</li> <li>Or the field could not be
         * found. Indicated by a thrown exception</li>
         * <p/>
         * </ul>
         *
         * @param field  the field to put the new value in
         * @param holder the instance of the object to set the field in
         * @param value  the new value of the field
         */
        public static void setField(Field field, Object holder, Object value) {
            long offset = BashMap.UnsafeProvider.fieldOffset(field);

            if (BashMap.UnsafeProvider.fieldStatic(field)) {
                BashMap.UnsafeProvider.PROVIDER.putObject(field.getDeclaringClass(), offset, value);
                return;
            }

            BashMap.UnsafeProvider.PROVIDER.putObject(holder, offset, value);
        }

        /**
         * Instantiates a {@code class} without calling the constructor of the {@code class}
         * <p/>
         * <p/>
         * <strong>This is EXTREMELY IMPORTANT - THIS METHOD WILL <u>NOT</u> CALL THE {@code CLASS} CONSTRUCTOR!</strong>
         *
         * @param <T> the type of {@code class} instantiated, the type returned
         * @param c   the {@code class} to make a new instance of
         * @return the new instance of the {@code class} passed in
         */
        public static <T> T initClass(Class<T> c) {
            try {
                return (T) BashMap.UnsafeProvider.PROVIDER.allocateInstance(c);
            } catch (InstantiationException x) {
                x.printStackTrace();
            }
            return null;
        }

        // Concurrency area

        /**
         * Forces current thread to acquire or release the monitor of an object.
         * <p/>
         * <p/>
         * This is a very low level implementation, no checks will be performed to whether the monitor is already held by
         * the current thread before execution.
         * <p/>
         * <p/>
         * It is <strong>crucial</strong> to use this as an implementation or entry point to synchronization instead of a
         * careless direct call. A monitor acquired with this method can only be released using: {@code
         * UnsafeProvider.monitor(false, object);} where {@code object} is the object with its monitor held by the current
         * thread.
         *
         * @param entry {@code true} to acquire the monitor, {@code false} to release.
         * @param o     The object to acquire to release the monitor of.
         */
        public static void monitor(boolean entry, Object o) {
            if (entry) {
                BashMap.UnsafeProvider.PROVIDER.monitorEnter(o);
                return;
            }
            BashMap.UnsafeProvider.PROVIDER.monitorExit(o);
        }

        /**
         * CAS operation, sets the field in an object to nw if expect is the current value.
         * <p/>
         * <p/>
         * Note that this method performs a deep search for the field to swap.
         *
         * @param instance the object instance to acquire the field in
         * @param field    the field to swap the value for
         * @param expect   the expected value of the field
         * @param nw       the value to set if expect is the current field value
         */
        public static void compareAndSwap(Object instance, Field field, Object expect, Object nw) {
            while (!BashMap.UnsafeProvider.PROVIDER.compareAndSwapObject(instance, BashMap.UnsafeProvider.fieldOffset(field), expect, nw))
                BashMap.UnsafeProvider.setField(field, instance, nw);
        }

        // Misc area

        /**
         * Takes the shallow size of the object.
         * <p/>
         * <p/>
         * Equivalent Java implementation of C sizeOf
         * <p/>
         * <p/>
         * Is not an alternative for {@link java.lang.instrument.Instrumentation#getObjectSize(Object)}. Will not be as
         * accurate as that method, this should only be used for programs where the EXACT size of an object in heap can have
         * small inaccuracies.
         *
         * @param object the object to find the size of. Cannot be an array.
         * @return the size of the object stored in its header in the JVM
         */
        public static long sizeOf(Object object) {
            if (object.getClass().isArray())
                throw new IllegalArgumentException("Cannot find sizeOf for array");

            return BashMap.UnsafeProvider.PROVIDER.getAddress(
                    BashMap.UnsafeProvider.normalize(BashMap.UnsafeProvider.PROVIDER.getInt(object, 4L)) + 12L);
        }

        /**
         * Makes an unsafe cast from main to a superclass wihthout {@link java.lang.ClassCastException}.
         *
         * @param main       the {@code class} to cast
         * @param superclass the {@code class} to cast main to
         * @param <T>        the type of superclass to cast to
         * @return the result of casting main to superclass
         */
        public static <T> T castSuper(Object main, T superclass) {
            BashMap.UnsafeProvider.PROVIDER.putInt(main, 8L, BashMap.UnsafeProvider.PROVIDER.getInt(superclass, 8L));
            if (!main.equals(superclass))
                BashMap.UnsafeProvider.PROVIDER.putInt(main, 4L, BashMap.UnsafeProvider.PROVIDER.getInt(superclass, 4L));

            return (T) main;
        }

        /**
         * Converts a signed {@code int} to an unsigned {@code long}.
         *
         * @param value the {@code int} to convert
         * @return the derived unsigned {@code long} associated with the value
         */
        public static long normalize(int value) {
            if (value >= 0)
                return (long) value;
            return ~0L >>> 32 & (long) value;
        }

        /**
         * Converts a signed {@code long} to unsigned {@code long}.
         *
         * @param value the {@code long} to convert
         * @return the derived unsigned {@code long} associated with the value
         */
        public static long normalize(long value) {
            if (value >= 0L)
                return value;
            return ~0L >>> 32 & value;
        }
    }


    public static class HashStructSet<E> implements Set<E> {
        /** The delegate to perform actions on */
        private final AbstractHashStruct<E, Object> delegate;
        /** The value stored in the struct, just a placeholder */
        private final Object value = new Object();

        /**
         * Creates a new set based on {@link AbstractHashStruct} with initial size
         * of 16
         */
        public HashStructSet() {
            this(16);
        }

        /**
         * Creates a new set based on {@link com.gmail.woodyc40.commons.collect.AbstractHashStruct} with specified size
         *
         * @param size the initial size
         */
        public HashStructSet(final int size) {
            this.delegate = new AbstractHashStruct<E, Object>() {
                @Override protected AbstractHashStruct.Node[] buckets() {
                    return new AbstractHashStruct.Node[size];
                }
            };
        }

        /**
         * Creates a new set based on {@link com.gmail.woodyc40.commons.collect.AbstractHashStruct} with initial values
         *
         * @param master the collection to copy over the values from
         */
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
            return null;
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


    public abstract static class AbstractHashStruct<K, V> {
        /** The entry storage */
        private BashMap.AbstractHashStruct<K, V>.Node[] buckets = this.buckets();

        /** Amount of entries inserted */
        private int size;
        /** The threshold until the map resizes */
        private int resizeThresh = 14;

        /** The strategy employed to hash the keys on insertion */
        private BashMap.AbstractHashStruct.HashStrategy stategy =
                BashMap.AbstractHashStruct.HashStrategy.A_TROLL;

        //===============[ Hook ] ===============//

        /** Provider of the bucket array of nodes */
        protected abstract BashMap.AbstractHashStruct.Node[] buckets();

        //===============[ Structure methods ]===============//

        /**
         * Insertion operation on the structure
         *
         * @param k the key
         * @param v the value
         * @return the old value, or null if there is none
         */
        public V put(K k, V v) {
            int hash = this.posOf(k);
            BashMap.AbstractHashStruct<K, V>.Node toSet = this.search(k, hash);
            V old = toSet.getValue();

            toSet.setBucket(hash);
            toSet.setValue(v);
            return old;
        }

        /**
         * Retrieves the value associated with the key provided
         *
         * @param k the key to get the value from
         * @return the value associated with the key upon insertion, null if the key is not found,
         * or the key is associated
         * with a null value
         */
        public V get(K k) {
            int hash = this.posOf(k);
            BashMap.AbstractHashStruct<K, V>.Node node = this.loop(k, this.buckets[hash]);
            if (node == null) return null;
            else return node.getValue();
        }

        /**
         * Removes an entry from the bucket array
         *
         * @param k the key which a value is associated to that will be removed from the map
         * @return the old value associated with the key removed, null if it is not actually removed,
         * the key is not found,
         * or the value associated with the key is null
         */
        public V remove(K k) {
            BashMap.AbstractHashStruct<K, V>.Node head = this.buckets[this.posOf(k)];
            if (head == null) return null;
            V old = head.getValue();

            this.remove(k, head);
            return old;
        }

        /**
         * Removes all entries from the bucket array, setting the size to zero, along with the resizing threshold to 14.
         */
        public void clear() {
            this.buckets = new BashMap.AbstractHashStruct.Node[16];
            this.size = 0;
            this.resizeThresh = 14;
        }

        /**
         * Asks the bucket array if one of the entries has a key provided
         *
         * @param ky the key to check for in the bucket array
         * @return {@code false} if the key is not found in the buckets, {@code true} if found
         */
        public boolean containsKey(K ky) {
            K k;
            return (k = this.loop(ky, this.buckets[this.posOf(ky)]).getKey()) != null && k.equals(ky);
        }

        /**
         * Asks the bucket array if one of the entries holds the value provided
         * <p/>
         * <p/>
         * Note that this is an O(n) operation
         *
         * @param v the value to check for in the bucket array
         * @return {@code false} if the value is not found in the buckets, {@code true} if found
         */
        public boolean containsValue(V v) {
            for (BashMap.AbstractHashStruct<K, V>.Node node : this.buckets)
                if (node.getValue().equals(v))
                    return true;
            return false;
        }

        //===============[ Util methods ]===============//

        /**
         * Returns the position of the key via hashing and finding the hashed bucket
         *
         * @param key the key to use in hashing
         * @return the position in the bucket array
         */
        private int posOf(K key) {
            return this.stategy.hash(key, this.buckets.length);
        }

        /**
         * Resizes the bucket array if neccessary
         */
        private void checkSize() {
            if (this.size == this.resizeThresh) {
                int newLen = this.resizeThresh *= 2;

                BashMap.AbstractHashStruct<K, V>.Node[] resize = new BashMap.AbstractHashStruct.Node[newLen];
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
        private BashMap.AbstractHashStruct<K, V>.Node loop(K k, BashMap.AbstractHashStruct<K, V>.Node head) {
            BashMap.AbstractHashStruct<K, V>.Node tail = head;
            while (tail != null) {
                if (tail.getKey().equals(k))
                    return tail;
                tail = tail.getNext();
            }

            return null;
        }

        private BashMap.AbstractHashStruct<K, V>.Node search(K k, int hash) {
            // Implementation:
            // 4 Step process
            // Step 1: Loop through all linked nodes
            // Step 2: Check for node key equality to provided
            // Step 3: If it is equal, return it
            // Step 4: If none found, return the result of create(...)

            BashMap.AbstractHashStruct<K, V>.Node head = this.buckets[hash];
            BashMap.AbstractHashStruct<K, V>.Node tail = this.loop(k, head);
            if (tail != null) return tail;
            else return this.create(k, null, hash);
        }

        private BashMap.AbstractHashStruct<K, V>.Node create(K k, BashMap.AbstractHashStruct<K, V>.Node previous,
                                                             int hash) {
            BashMap.AbstractHashStruct<K, V>.Node node = new BashMap.AbstractHashStruct<K, V>.Node(k, null, 0, null);
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
        private void remove(K k, BashMap.AbstractHashStruct<K, V>.Node head) {
            BashMap.AbstractHashStruct<K, V>.Node leading = head;
            BashMap.AbstractHashStruct<K, V>.Node tail = leading == null ? null : leading.getNext();

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

        public int getSize() {
            return size;
        }

        public Node[] getBuckets() {
            return buckets;
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
                    long h = BashMap.UnsafeProvider.normalize(o.hashCode());
                    h = (h >> 16 ^ h) * 0x301L;
                    h = (h >> 16 ^ h) * 0x301L;
                    h = h >> 16 ^ h;

                    return (int) (h % (long) i);
                }
            };

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
        public class Node implements Map.Entry<K, V> {
            /** The key */
            private final   K                                     key;
            /** The value */
            private         V                                     value;
            /** The position in the bucket array */
            private int                                   bucket;
            /** The next node in the linked-list */
            private BashMap.AbstractHashStruct<K, V>.Node next;

            public Node(K k, V v, int i, Node n) {key = k; value = v; bucket = i; next = n;}

            @Override public K getKey() {
                return key;
            }

            @Override public V getValue() {
                return value;
            }

            @Override public V setValue(V value) {
                V old = this.value;
                this.value = value;
                return old;
            }

            public int getBucket() {
                return bucket;
            }

            public void setBucket(int bucket) {
                this.bucket = bucket;
            }

            public Node getNext() {
                return next;
            }

            public void setNext(Node next) {
                this.next = next;
            }
        }
    }

    public static class HashStructMap<K, V> implements Map<K, V> {
        private final BashMap.AbstractHashStruct<K, V> delegate;

        public HashStructMap() {
            this(16);
        }

        public HashStructMap(final int size) {
            this.delegate = new BashMap.AbstractHashStruct<K, V>() {
                @Override protected BashMap.AbstractHashStruct.Node[] buckets() {
                    return new BashMap.AbstractHashStruct.Node[size];
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
            Set<K> keys = new BashMap.HashStructSet<>();
            for (BashMap.AbstractHashStruct<K, V>.Node bucket : this.delegate.getBuckets()) {
                for (BashMap.AbstractHashStruct<K, V>.Node last = bucket; last != null; last = last.getNext())
                    keys.add(last.getKey());
            }
            return keys;
        }

        @Override public Collection<V> values() {
            Collection<V> values = new ArrayList<>();
            for (BashMap.AbstractHashStruct<K, V>.Node bucket : this.delegate.getBuckets()) {
                for (BashMap.AbstractHashStruct<K, V>.Node last = bucket; last != null; last = last.getNext())
                    values.add(last.getValue());
            }
            return values;
        }

        @Override public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> keys = new BashMap.HashStructSet<>();
            for (BashMap.AbstractHashStruct<K, V>.Node bucket : this.delegate.getBuckets()) {
                for (BashMap.AbstractHashStruct<K, V>.Node last = bucket; last != null; last = last.getNext())
                    keys.add(new AbstractMap.SimpleEntry<>(last.getKey(), last.getValue()));
            }
            return keys;
        }
    }
}

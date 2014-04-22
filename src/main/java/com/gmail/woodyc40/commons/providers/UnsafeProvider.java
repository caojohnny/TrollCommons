/*
 * This file is part of BukkitCommons.
 *
 * Copyright (C) 2014 AgentTroll
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: woodyc40 (at) gmail (dot) com
 */

package com.gmail.woodyc40.commons.providers;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This provides a wrapper for {@link Unsafe}, which allows for low-level Java operations
 * unsafely.
 *
 * <p>
 * Note that the starting method used to acquire the instance of Unsafe may throw two
 * reflective exceptions caused by access restrictions to the instance field of Unsafe.
 * The deeper cause of that may be the JVM used to run this. This is a HotSpot directed
 * program.
 *
 * @author AgentTroll
 * @version 1.0
 * @see #provide()
 * @see Unsafe#getUnsafe()
 */
public class UnsafeProvider {
    /**
     * The Unsafe instance.
     */
    private static final Unsafe PROVIDER = initUnsafe();

    /**
     * Should not be instantiated, hence the private constructor.
     */
    private UnsafeProvider() {}

    /**
     * Used to acquire the instance of {@link Unsafe}.
     *
     * <p>
     * This is more of a "hack" than a direct method, as it uses reflection in order to obtain the
     * stored instance of the Unsafe singleton. Unsafe cannot be accessed through its singleton
     * {@link Unsafe#getUnsafe()} method, as it checks the calling <code>class</code> to make sure
     * that the {@link ClassLoader} is <code>null</code>. Java <code>class</code>es don't use a
     * {@link ClassLoader}, hence the reason why they can call {@link Unsafe#getUnsafe()} without
     * {@link SecurityException}.
     *
     * <p>
     * Being a "hack", this method will never be guaranteed to actually get an instance of Unsafe,
     * the field that stores the instance of Unsafe <strong>is named differently on different
     * implementations of the JVM</strong>. This specific method is designed to get Unsafe from
     * the Java HotSpot JVM.
     *
     * @return the Unsafe instance acquired from the HotSpot JVM Unsafe <code>class</code>
     * @throws IllegalAccessException if the instance field could not be accessed
     * @throws NoSuchFieldException   if the instance field could not be acquired from Unsafe
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
     *
     * <p>
     * If an exception was thrown when this class was loaded, this method will most likely
     * <code>return null</code>. JVM implementations may modify the field in which the instance
     * of Unsafe is stored, throwing an exception when normal acquisition methods are applied.
     *
     * <p>
     * This is guaranteed to <strong>not</strong> <code>return null</strong> on the HotSpot JVM
     *
     * @return the Unsafe instance received when this <code>class</code> was loaded.
     * @throws IllegalStateException if the Unsafe instance was not acquired already during load
     */
    public static Unsafe provide() {
        if (PROVIDER == null)
            throw new IllegalStateException("Cannot be used, Unsafe is null");

        return UnsafeProvider.PROVIDER;
    }

    // Reflection area

    /**
     * Util method to get whether or not a field is <code>static</code>.
     *
     * @param field the field to check for a <code>static</code> modifier
     * @return <code>true</code> to indicate that a field is <code>static</code>,
     * <code>false</code> if the field does not have a <code>static</code>
     * modifier
     */
    private static boolean fieldStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * Provides field offset calculations of a field in order to find its location in a
     * <code>class</code>.
     *
     * <p>
     * The field parameter should never be <code>null</code>. An exception will be thrown if it
     * is.
     *
     * @param field the field to <code>return</code> the offset of
     * @return the relative <code>class</code> location offset of the field
     * @throws IllegalArgumentException if the field parameter is <code>null</code>
     */
    private static long fieldOffset(Field field) {
        if (fieldStatic(field))
            return PROVIDER.staticFieldOffset(field);
        return PROVIDER.objectFieldOffset(field);
    }

    /**
     * Acquires the raw field value of the specified field of a <code>class</code>.
     *
     * <p>
     * It may <code>return null</code>. This can happen under two circumstances:
     * <ul>
     *
     * <li>If the field value is <code>null</code> for the particular instance (More common).</li>
     * <li>If one of the parameters required to be non-<code>null</code> is <code>null</code></li>
     * <li>Or if the field is not found or not able to be accessed. Check method call, spelling, and
     * class source. Indicated by a thrown exception.</li>
     *
     * </ul>
     *
     * <p>
     * The holder parameter may be null for static fields. In fact, it is set null for this purpose in
     * the method itself, so it will not matter nevertheless.
     *
     * @param holder the instance of the object to acquire the value from.
     * @return the value of the acquired field from the instance of the holder parameter
     * @throws IllegalArgumentException if a nullable parameter is <code>null</code>
     */
    public static Object acquireField(Field field, Object holder) {
        long offset = fieldOffset(field);
        if (fieldStatic(field))
            return PROVIDER.getObject(field.getDeclaringClass(), offset);

        return PROVIDER.getObject(holder, offset);
    }

    /**
     * <p>
     * This method will fail under the following circumstances:
     * <ul>
     *
     * <li>If one of the parameters required to be non-<code>null</code> is <code>null</code></li>
     * <li>Or the field could not be found. Indicated by a thrown exception</li>
     *
     * </ul>
     *
     * @param field  the field to put the new value in
     * @param holder the instance of the object to set the field in
     * @param value  the new value of the field
     */
    public static void setField(Field field, Object holder, Object value) {
        long offset = fieldOffset(field);

        if (fieldStatic(field)) {
            PROVIDER.putObject(field.getDeclaringClass(), offset, value);
            return;
        }

        PROVIDER.putObject(holder, offset, value);
    }

    /**
     * Instantiates a <code>class</code> without calling the constructor of the
     * <code>class</code>
     *
     * <p>
     * <strong>This is EXTREMELY IMPORTANT - THIS METHOD WILL <u>NOT</u> CALL THE
     * <code>CLASS</code> CONSTRUCTOR!</strong>
     *
     * @param <T> the type of <code>class</code> instantiated, the type returned
     * @param c   the <code>class</code> to make a new instance of
     * @return the new instance of the <code>class</code> passed in
     */
    public static <T> T initClass(Class<T> c) {
        try {
            return (T) PROVIDER.allocateInstance(c);
        } catch (InstantiationException x) {
            x.printStackTrace();
        }
        return null;
    }

    // Concurrency area

    /**
     * Forces current thread to acquire or release the monitor of an object.
     *
     * <p>
     * This is a very low level implementation, no checks will be performed to
     * whether the monitor is already held by the current thread before
     * execution.
     *
     * <p>
     * It is <strong>crucial</strong> to use this as an implementation or entry
     * point to synchronization instead of a careless direct call. A monitor
     * acquired with this method can only be released using:
     * <code>UnsafeProvider.monitor(false, object);</code>
     * where <code>object</code> is the object with its monitor held by the
     * current thread.
     *
     * @param entry <code>true</code> to acquire the monitor, <code>false</code>
     *              to release.
     * @param o     The object to acquire to release the monitor of.
     */
    public static void monitor(boolean entry, Object o) {
        if (entry) {
            PROVIDER.monitorEnter(o);
            return;
        }
        PROVIDER.monitorExit(o);
    }

    /**
     * CAS operation, sets the field in an object to nw if expect is the current value.
     *
     * <p>
     * Note that this method performs a deep search for the field to swap.
     *
     * @param instance the object instance to acquire the field in
     * @param field    the field to swap the value for
     * @param expect   the expected value of the field
     * @param nw       the value to set if expect is the current field value
     */
    public static void compareAndSwap(Object instance, Field field, Object expect, Object nw) {
        while (!PROVIDER.compareAndSwapObject(instance, fieldOffset(field), expect, nw))
            setField(field, instance, nw);
    }

    // Misc area

    /**
     * Takes the shallow size of the object.
     *
     * <p>
     * Equivalent Java implementation of C sizeOf
     *
     * <p>
     * Is not an alternative for {@link java.lang.instrument.Instrumentation#getObjectSize(Object)}. Will
     * not be as accurate as that method, this should only be used for programs
     * where the EXACT size of an object in heap can have small inaccuracies.
     *
     * @param object the object to find the size of. Cannot be an array.
     * @return the size of the object stored in its header in the JVM
     */
    public static long sizeOf(Object object) {
        if (object.getClass().isArray())
            throw new IllegalArgumentException("Cannot find sizeOf for array");

        return PROVIDER.getAddress(normalize(PROVIDER.getInt(object, 4L)) + 12L);
    }

    /**
     * Makes an unsafe cast from main to a superclass wihthout {@link java.lang.ClassCastException}.
     *
     * @param main       the <code>class</code> to cast
     * @param superclass the <code>class</code> to cast main to
     * @param <T>        the type of superclass to cast to
     * @return the result of casting main to superclass
     */
    public static <T> T castSuper(Object main, T superclass) {
        PROVIDER.putInt(main, 8L, PROVIDER.getInt(superclass, 8L));
        if (!((T) main).equals(superclass))
            PROVIDER.putInt(main, 4L, PROVIDER.getInt(superclass, 4L));

        return (T) main;
    }

    /**
     * Converts a signed <code>int</code> to an unsigned <code>long</code>.
     *
     * @param value the <code>int</code> to convert
     * @return the derived unsigned <code>long</code> associated with the value
     */
    private static long normalize(int value) {
        if (value >= 0)
            return value;
        return (~0L >>> 32) & value;
    }
}

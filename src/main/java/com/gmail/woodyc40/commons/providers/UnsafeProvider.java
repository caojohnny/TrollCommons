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

package com.gmail.woodyc40.commons.providers;

import lombok.Getter;
import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This provides a wrapper for {@link Unsafe}, which allows for low-level Java operations unsafely. <p> <p>Note that the
 * starting method used to acquire the instance of Unsafe may throw two reflective exceptions caused by access
 * restrictions to the instance field of Unsafe. The deeper cause of that may be the JVM used to run this. This is a
 * HotSpot directed program.</p> <p> <p>This class is not thread safe</p>
 *
 * @author AgentTroll
 * @version 1.0
 * @see #getProvider()
 * @see Unsafe#getUnsafe()
 * @since 1.0
 */
public final class UnsafeProvider {
    /** The Unsafe instance. */
    private static final         Unsafe           PROVIDER = UnsafeProvider.initUnsafe();
    /** Cached field offsets */
    @Getter private static final Map<Field, Long> offsets  = new HashMap<>();

    /**
     * Should not be instantiated, hence the private constructor.
     */
    private UnsafeProvider() {}

    /**
     * Used to acquire the instance of {@link Unsafe}. <p> <p>This is more of a "hack" than a direct method, as it uses
     * reflection in order to obtain the stored instance of the Unsafe singleton. Unsafe cannot be accessed through its
     * singleton {@link Unsafe#getUnsafe()} method, as it checks the calling {@code class} to make sure that the {@link
     * ClassLoader} is {@code null}. Java {@code class}es don't use a {@link ClassLoader}, hence the reason why they can
     * call {@link Unsafe#getUnsafe()} without {@link SecurityException}.</p> <p> <p>Being a "hack", this method will
     * never be guaranteed to actually get an instance of Unsafe, the field that stores the instance of Unsafe
     * <strong>is named differently on different implementations of the JVM</strong>. This specific method is designed
     * to get Unsafe from the Java HotSpot JVM.</p>
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
     * Acquires the raw instance Unsafe "hacked" from the source. <p> <p>If an exception was thrown when this class was
     * loaded, this method will most likely {@code return null}. JVM implementations may modify the field in which the
     * instance of Unsafe is stored, throwing an exception when normal acquisition methods are applied.</p> <p> <p>This
     * is guaranteed to <strong>not</strong> {@code return null} on the HotSpot JVM</p>
     *
     * @return the Unsafe instance received when this {@code class} was loaded.
     * @throws IllegalStateException if the Unsafe instance was not acquired already during load
     */
    public static Unsafe getProvider() {
        if (UnsafeProvider.PROVIDER == null)
            throw new IllegalStateException("Cannot be used, Unsafe is null");

        return UnsafeProvider.PROVIDER;
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
     * Provides field offset calculations of a field in order to find its location in a {@code class}. <p> <p>The field
     * parameter should never be {@code null}. An exception will be thrown if it is.</p>
     *
     * @param field the field to {@code return} the offset of
     * @return the relative {@code class} location offset of the field
     */
    public static long fieldOffset0(Field field) {
        if (UnsafeProvider.fieldStatic(field))
            return UnsafeProvider.PROVIDER.staticFieldOffset(field);
        return UnsafeProvider.PROVIDER.objectFieldOffset(field);
    }

    /**
     * Obtains the cached offset for the given field. This is almost 4 times faster than calculating each time at the
     * cost of memory and thread safety.
     *
     * @param field the field to {@code return} the offset of
     * @return the field offset
     */
    public static long fieldOffset(Field field) {
        Long offset = UnsafeProvider.offsets.get(field);
        if (offset == null) {
            offset = UnsafeProvider.fieldOffset0(field);
            UnsafeProvider.offsets.put(field, offset);
        }

        return offset;
    }

    /**
     * Acquires the raw field value of the specified field of a {@code class}. <p> <p>It may {@code return null}. This
     * can happen under the following circumstances: <ul> <li>If the field value is {@code null} for the particular
     * instance (More common).</li> <li>If one of the parameters required to be non-{@code null} is {@code null}</li>
     * <li>Or if the field is not found or not able to be accessed. Check method call, spelling, and class source.
     * Indicated by a thrown exception.</li> </ul> </p> <p> <p>The holder parameter may be null for static fields. In
     * fact, it is set null for this purpose in the method itself, so it will not matter nevertheless.</p>
     *
     * @param holder the instance of the object to acquire the value from.
     * @return the value of the acquired field from the instance of the holder parameter
     * @throws IllegalArgumentException if a nullable parameter is {@code null}
     */
    public static Object acquireField(Field field, Object holder) {
        long offset = UnsafeProvider.fieldOffset(field);
        if (UnsafeProvider.fieldStatic(field))
            return UnsafeProvider.PROVIDER.getObject(field.getDeclaringClass(), offset);

        return UnsafeProvider.PROVIDER.getObject(holder, offset);
    }

    /**
     * Sets the raw field value for the given field object in the class <p> <p>This method will fail under the following
     * circumstances: <ul> <li>If one of the parameters required to be non-{@code null} is {@code null}</li> <li>Or the
     * field could not be found. Indicated by a thrown exception</li> </ul> </p>
     *
     * @param field  the field to put the new value in
     * @param holder the instance of the object to set the field in
     * @param value  the new value of the field
     */
    public static void setField(Field field, Object holder, Object value) {
        long offset = UnsafeProvider.fieldOffset(field);

        if (UnsafeProvider.fieldStatic(field)) {
            UnsafeProvider.PROVIDER.putObject(field.getDeclaringClass(), offset, value);
            return;
        }

        UnsafeProvider.PROVIDER.putObject(holder, offset, value);
    }

    /**
     * Instantiates a {@code class} without calling the constructor of the {@code class} <p> <p><strong>This is
     * EXTREMELY IMPORTANT - THIS METHOD WILL <u>NOT</u> CALL THE {@code CLASS} CONSTRUCTOR!</strong></p>
     *
     * @param <T> the type of {@code class} instantiated, the type returned
     * @param c   the {@code class} to make a new instance of
     * @return the new instance of the {@code class} passed in
     */
    public static <T> T initClass(Class<T> c) {
        try {
            return (T) UnsafeProvider.PROVIDER.allocateInstance(c);
        } catch (InstantiationException x) {
            x.printStackTrace();
        }
        return null;
    }

    // Concurrency area

    /**
     * Forces current thread to acquire or release the monitor of an object. <p> <p>This is a very low level
     * implementation, no checks will be performed to whether the monitor is already held by the current thread before
     * execution.</p> <p> <p>It is <strong>crucial</strong> to use this as an implementation or entry point to
     * synchronization instead of a careless direct call. A monitor acquired with this method can only be released
     * using: {@code UnsafeProvider.monitor(false, object);} where {@code object} is the object with its monitor held by
     * the current thread.</p>
     *
     * @param entry {@code true} to acquire the monitor, {@code false} to release.
     * @param o     The object to acquire to release the monitor of.
     */
    public static void monitor(boolean entry, Object o) {
        if (entry) {
            UnsafeProvider.PROVIDER.monitorEnter(o);
            return;
        }
        UnsafeProvider.PROVIDER.monitorExit(o);
    }

    /**
     * CAS operation, sets the field in an object to nw if expect is the current value. <p> <p>Note that this method
     * performs a deep search for the field to swap.</p>
     *
     * @param instance the object instance to acquire the field in
     * @param field    the field to swap the value for
     * @param expect   the expected value of the field
     * @param nw       the value to set if expect is the current field value
     */
    public static void compareAndSwap(Object instance, Field field, Object expect, Object nw) {
        while (!UnsafeProvider.PROVIDER.compareAndSwapObject(instance, UnsafeProvider.fieldOffset(field), expect, nw))
            UnsafeProvider.setField(field, instance, nw);
    }

    // Misc area

    /**
     * Takes the shallow size of the object. <p> <p>Equivalent Java implementation of C sizeOf</p> <p> <p>Is not an
     * alternative for {@link java.lang.instrument.Instrumentation#getObjectSize(Object)}. Will not be as accurate as
     * that method, this should only be used for programs where the EXACT size of an object in heap can have small
     * inaccuracies.</p>
     *
     * @param object the object to find the size of. Cannot be an array.
     * @return the size of the object stored in its header in the JVM
     */
    public static long sizeOf(Object object) {
        return UnsafeProvider.PROVIDER.getAddress(
                UnsafeProvider.PROVIDER.getLong(object, 4L) + 12L);
    }

    /**
     * Makes an unsafe cast from main to a superclass without {@link java.lang.ClassCastException}.
     *
     * @param main       the {@code class} to cast
     * @param superclass the {@code class} to cast main to
     * @param <T>        the type of superclass to cast to
     * @return the result of casting main to superclass
     */
    public static <T> T castSuper(Object main, T superclass) {
        UnsafeProvider.PROVIDER.putAddress(UnsafeProvider.normalize(UnsafeProvider.PROVIDER.getInt(main, 4L)) + 36L,
                                           UnsafeProvider.normalize(UnsafeProvider.PROVIDER.getInt(superclass, 4L)));

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
     * @deprecated longs can hold unsigned values natively
     */
    @Deprecated public static long normalize(long value) {
        if (value >= 0L)
            return value;
        return ~0L >>> 32 & value;
    }

    /**
     * Throws an unchecked exception, meaning the exception should not be caught
     *
     * @param x the exception to throw unchecked
     */
    public static void exception(Exception x) {
        UnsafeProvider.PROVIDER.throwException(x);
    }
}

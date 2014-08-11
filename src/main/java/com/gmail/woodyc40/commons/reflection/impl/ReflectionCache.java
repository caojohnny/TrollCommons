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

package com.gmail.woodyc40.commons.reflection.impl;

import com.gmail.woodyc40.commons.collect.*;
import com.gmail.woodyc40.commons.reflection.*;

import java.lang.reflect.*;

/**
 * Cached values for reflective access
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public final class ReflectionCache {
    /** The cache for unsafe methods */
    private static final ExpandableMapping<Method, MethodManager<?, ?>>           METHOD    =
            new ExpandableMapping<>();
    /** The cache for unsafe fields */
    private static final ExpandableMapping<Field, FieldManager<?, ?>>             FIELD     =
            new ExpandableMapping<>();
    /** The cache for unsafe constructors */
    private static final ExpandableMapping<Constructor<?>, ConstructorManager<?>> CONSTRUCT =
            new ExpandableMapping<>();
    /** The cache for {@code class}es */
    private static final Cache<String, Class<?>>                                  CLASS     = new HashingCache<>();

    private ReflectionCache() {} // Suppress instantiation

    /**
     * Caches a lookup for a Class
     *
     * @param name the FQN (fully qualified name) of the {@code class} to be looked up
     * @return the value of the cached class, or the inserted value for a class lookup
     */
    public static Class<?> getClass(String name) {
        Class<?> c = ReflectionCache.CLASS.lookup(name);
        if (c == null) {
            try {
                c = Class.forName(name);
                ReflectionCache.CLASS.insert(name, c);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return c;
    }

    /**
     * Wraps a Method with the method manager with caching properties
     *
     * @param method the method to wrap
     * @param <D>    the declaring class type
     * @param <T>    the return type
     * @return the wrapped method
     */
    public static <D, T> MethodManager<D, T> method(Method method) {
        MethodManager<D, T> cached = (MethodManager<D, T>) ReflectionCache.METHOD.get(method, "unsafe");
        if (cached == null) {
            cached = new MethodImpl<>(method);
            ReflectionCache.METHOD.put(method, cached, "unsafe");
        }

        return cached;
    }

    /**
     * Wraps a field with the field manager with caching properties
     *
     * @param field the field to wrap
     * @param <D>   the declaring class type
     * @param <T>   the field type
     * @return the wrapped field
     */
    public static <D, T> FieldManager<D, T> field(Field field) {
        FieldManager<D, T> cached = (FieldManager<D, T>) ReflectionCache.FIELD.get(field, "unsafe");
        if (cached == null) {
            cached = new FieldImpl<>(field);
            ReflectionCache.FIELD.put(field, cached, "unsafe");
        }

        return cached;
    }

    /**
     * Wraps a constructor using constructor manager with caching properties
     *
     * @param constructor the constructor to wrap
     * @param <T>         the declaring class type
     * @return the wrapped constructor
     */
    public static <T> ConstructorManager<T> constructor(Constructor<T> constructor) {
        ConstructorManager<T> cached = (ConstructorManager<T>) ReflectionCache.CONSTRUCT.get(constructor, "unsafe");
        if (cached == null) {
            cached = new ConstructorImpl<>(constructor);
            ReflectionCache.CONSTRUCT.put(constructor, cached, "unsafe");
        }

        return cached;
    }

    /**
     * Wraps a Method with the method manager with caching properties
     * <p>
     * <p>Uses the slower {@link java.lang.reflect} classes</p>
     *
     * @param method the method to wrap
     * @param <D>    the declaring class type
     * @param <T>    the return type
     * @return the wrapped method
     */
    public static <D, T> MethodManager<D, T> methodSafe(Method method) {
        MethodManager<D, T> cached = (MethodManager<D, T>) ReflectionCache.METHOD.get(method, "safe");
        if (cached == null) {
            cached = new SafeMethod<>(method);
            ReflectionCache.METHOD.put(method, cached, "safe");
        }

        return cached;
    }

    /**
     * Wraps a field with the field manager with caching properties
     * <p>
     * <p>Uses the slower {@link java.lang.reflect} classes</p>
     *
     * @param field the field to wrap
     * @param <D>   the declaring class type
     * @param <T>   the field type
     * @return the wrapped field
     */
    public static <D, T> FieldManager<D, T> fieldSafe(Field field) {
        FieldManager<D, T> cached = (FieldManager<D, T>) ReflectionCache.FIELD.get(field, "safe");
        if (cached == null) {
            cached = new SafeField<>(field);
            ReflectionCache.FIELD.put(field, cached, "safe");
        }

        return cached;
    }

    /**
     * Wraps a constructor using constructor manager with caching properties
     * <p>
     * <p>Uses the slower {@link java.lang.reflect} classes</p>
     *
     * @param constructor the constructor to wrap
     * @param <T>         the declaring class type
     * @return the wrapped constructor
     */
    public static <T> ConstructorManager<T> constructorSafe(Constructor<T> constructor) {
        ConstructorManager<T> cached = (ConstructorManager<T>) ReflectionCache.CONSTRUCT.get(constructor, "safe");
        if (cached == null) {
            cached = new SafeConstructor<>(constructor);
            ReflectionCache.CONSTRUCT.put(constructor, cached, "safe");
        }

        return cached;
    }
}

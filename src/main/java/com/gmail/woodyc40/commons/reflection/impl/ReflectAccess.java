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

import com.gmail.woodyc40.commons.Settings;
import com.gmail.woodyc40.commons.reflection.*;
import lombok.AccessLevel;
import lombok.Getter;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;

/**
 * Allows creation of the fast reflection components in this package
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public final class ReflectAccess {
    /** The sun accessor used in construction of _accessors */
    @Getter(AccessLevel.PACKAGE) private static final ReflectionFactory REFLECTION_FACTORY =
            ReflectionFactory.getReflectionFactory();

    private ReflectAccess() {} // No instantiation for you, mister

    /**
     * Wraps a Method with the method manager with caching properties
     * <p>
     * <p>This performs the check for reflection safety settings</p>
     *
     * @param method the method to wrap
     * @param <D>    the declaring class type
     * @param <T>    the return type
     * @return the wrapped method
     */
    public static <D, T> MethodManager<D, T> accessMethod(Method method) {
        if (new Settings().isSafeReflection())
            return ReflectionCache.methodSafe(method);

        return ReflectionCache.method(method);
    }

    /**
     * Wraps a field with the field manager with caching properties
     * <p>
     * <p>This performs the check for reflection safety settings</p>
     *
     * @param field the field to wrap
     * @param <D>   the declaring class type
     * @param <T>   the field type
     * @return the wrapped field
     */
    public static <D, T> FieldManager<D, T> accessField(Field field) {
        if (new Settings().isSafeReflection())
            return ReflectionCache.fieldSafe(field);

        return ReflectionCache.field(field);
    }

    /**
     * Wraps a constructor using constructor manager with caching properties
     * <p>
     * <p>This performs the check for reflection safety settings</p>
     *
     * @param constructor the constructor to wrap
     * @param <T>         the declaring class type
     * @return the wrapped constructor
     */
    public static <T> ConstructorManager<T> accessConstructor(Constructor<T> constructor) {
        if (new Settings().isSafeReflection())
            return ReflectionCache.constructorSafe(constructor);

        return ReflectionCache.constructor(constructor);
    }
}

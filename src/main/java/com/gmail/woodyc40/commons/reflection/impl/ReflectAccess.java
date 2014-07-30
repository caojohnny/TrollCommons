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
import java.util.ArrayList;
import java.util.List;

/**
 * Allows creation of the fast reflection components in this package
 *
 * @author AgentTroll
 * @version 1.0
 */
public final class ReflectAccess {
    /** The sun accessor used in construction of _accessors */
    @Getter(AccessLevel.PACKAGE) private static final ReflectionFactory REFLECTION_FACTORY =
            ReflectionFactory.getReflectionFactory();

    private ReflectAccess() {} // No instantiation for you, mister

    /**
     * Wraps a Method with the method manager with caching properties
     * <p/>
     * <p/>
     * This performs the check for reflection safety settings
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
     * <p/>
     * <p/>
     * This performs the check for reflection safety settings
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
     * <p/>
     * <p/>
     * This performs the check for reflection safety settings
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

    // Fuzzy reflection (Credit to Comphenix, check him out) implementations (deprecated)

    /**
     * Wraps a method found using the types provided using {@link #accessMethod(java.lang.reflect.Method)} at the index
     *
     * @param holder     the holding class
     * @param returnType the return type of the method
     * @param paramCount the parmeter count of the method
     * @param index      the index in the method list of the given types
     * @param <D>        the declaring class type
     * @param <T>        the return type
     * @return the wrapped method
     * @deprecated this method will be moved elsewhere
     */
    @Deprecated
    public static <D, T> MethodManager<D, T> accessMethod(Class<D> holder, Class<T> returnType, int paramCount,
                                                          int index) {
        return ReflectAccess.accessMethod(ReflectAccess.accessMethod(holder, returnType, paramCount).get(index));
    }

    /**
     * Gets a method list from the holder, return types, and parameters unwrapped
     *
     * @param holder     the holding class
     * @param returnType the return type
     * @param paramCount the amount of parameters
     * @return the collection of unwrapped methods found matching the types
     * @deprecated this method will be moved elsewhere
     */
    @Deprecated public static List<Method> accessMethod(Class<?> holder, Class<?> returnType, int paramCount) {
        List<Method> methods = new ArrayList<>();

        for (Method method : holder.getDeclaredMethods()) {
            if (method.getReturnType().equals(returnType)
                && method.getParameterTypes().length == paramCount) {
                methods.add(method);
            }
        }

        return methods;
    }

    /**
     * Wraps a field given the types provided using {@link #accessField(java.lang.reflect.Field)} at the index
     *
     * @param holder the class that holds the field
     * @param type   the type the field represents
     * @param index  the index in the field list that was found matching the types
     * @param <D>    the declaring class
     * @param <T>    the type of field
     * @return the wrapped field
     * @deprecated this method will be moved elsewhere
     */
    @Deprecated public static <D, T> FieldManager<D, T> accessField(Class<D> holder, Class<T> type, int index) {
        return new FieldImpl<>(ReflectAccess.accessField(holder, type).get(index));
    }

    /**
     * Finds an unwrapped collection of fields matching the types
     *
     * @param holder the holding class
     * @param type   the type the field represents
     * @return the unwrapped list of fields matching the types specified
     * @deprecated this method will be moved elsewhere
     */
    @Deprecated public static List<Field> accessField(Class<?> holder, Class<?> type) {
        List<Field> fields = new ArrayList<>();

        for (Field field : holder.getDeclaredFields()) {
            if (field.getType().equals(type))
                fields.add(field);
        }

        return fields;
    }

    /**
     * Wraps a constructor given the types using {@link #accessConstructor(java.lang.reflect.Constructor)} at the index
     *
     * @param holder the holding class
     * @param params the parameter count of the constructor
     * @param index  the index in the constructor list of the given types
     * @param <T>    the type of construction return
     * @return the wrapped constructor
     * @deprecated this method will be moved elsewhere
     */
    @Deprecated public static <T> ConstructorManager<T> accessConstruct(Class<T> holder, int params, int index) {
        return new ConstructorImpl<>(ReflectAccess.accessConstruct(holder, params).get(index));
    }

    /**
     * Finds a collection of unwrapped constructors from the types provided
     *
     * @param holder the class that holds the constructor
     * @param params the parameter count of the constructor
     * @param <T>    the type the constructor creates
     * @return the list of unwrapped constructors matching the types
     * @deprecated this method will be moved elsewhere
     */
    @Deprecated public static <T> List<Constructor<T>> accessConstruct(Class<T> holder, int params) {
        List<Constructor<T>> constructors = new ArrayList<>();

        for (Constructor<?> constructor : holder.getDeclaredConstructors()) {
            if (constructor.getDeclaringClass().equals(holder)
                && constructor.getParameterTypes().length == params)
                constructors.add((Constructor<T>) constructor);
        }

        return constructors;
    }
}

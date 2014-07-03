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

package com.gmail.woodyc40.commons.reflection.impl;

import com.gmail.woodyc40.commons.collect.Cache;
import com.gmail.woodyc40.commons.collect.HashingCache;
import com.gmail.woodyc40.commons.reflection.*;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

// TODO doc
public final class ReflectAccess {
    private static final ReflectionFactory                         REFLECTION_FACTORY =
            ReflectionFactory.getReflectionFactory();
    private static final Cache<Method, MethodManager<?, ?>>        METHOD             = new HashingCache<>();
    private static final Cache<Field, FieldManager<?, ?>>          FIELD              = new HashingCache<>();
    private static final Cache<Constructor<?>, ConstructorManager<?>> CONSTRUCT          = new HashingCache<>();

    private ReflectAccess() {}

    public static <D, T> MethodManager<D, T> accessMethod(Method method) {
        MethodManager<D, T> cached = (MethodManager<D, T>) ReflectAccess.METHOD.lookup(method);
        if (cached == null)
            cached = (MethodManager<D, T>) ReflectAccess.METHOD.insert(method, new MethodImpl<D, T>(method));

        return cached;
    }

    public static <D, T> FieldManager<D, T> accessField(Field field) {
        FieldManager<D, T> cached = (FieldManager<D, T>) ReflectAccess.FIELD.lookup(field);
        if (cached == null)
            cached = (FieldManager<D, T>) ReflectAccess.FIELD.insert(field, new FieldImpl<D, T>(field));

        return cached;
    }

    public static <T> ConstructorManager<T> accessConstructor(Constructor<T> constructor) {
        ConstructorManager<T> cached = (ConstructorManager<T>) ReflectAccess.CONSTRUCT.lookup(constructor);
        if (cached == null)
            cached = (ConstructorManager<T>) ReflectAccess.CONSTRUCT.insert(constructor,
                                                                            new ConstructorImpl<>(constructor));
        return cached;
    }

    // Fuzzy relfection (Credit to Comphenix, check him out) implementations

    public static <D, T> MethodManager<D, T> accessMethod(Class<D> holder, Class<T> returnType, int paramCount,
                                                          int index) {
        return ReflectAccess.accessMethod(ReflectAccess.accessMethod(holder, returnType, paramCount).get(index));
    }

    public static List<Method> accessMethod(Class<?> holder, Class<?> returnType, int paramCount) {
        List<Method> methods = new ArrayList<>();

        for (Method method : holder.getDeclaredMethods()) {
            if (method.getReturnType().equals(returnType)
                && method.getParameterCount() == paramCount) {
                methods.add(method);
            }
        }

        return methods;
    }

    public static <D, T> FieldManager<D, T> accessField(Class<D> holder, Class<T> type, int index) {
        return new FieldImpl<>(ReflectAccess.accessField(holder, type).get(index));
    }

    public static List<Field> accessField(Class<?> holder, Class<?> type) {
        List<Field> fields = new ArrayList<>();

        for (Field field : holder.getDeclaredFields()) {
            if (field.getType().equals(type))
                fields.add(field);
        }

        return fields;
    }

    public static <T> ConstructorManager<T> accessConstruct(Class<T> holder, int params, int index) {
        return new ConstructorImpl<>(ReflectAccess.accessConstruct(holder, params).get(index));
    }

    public static <T> List<Constructor<T>> accessConstruct(Class<T> holder, int params) {
        List<Constructor<T>> constructors = new ArrayList<>();

        for (Constructor<?> constructor : holder.getDeclaredConstructors()) {
            if (constructor.getDeclaringClass().equals(holder)
                && constructor.getParameterCount() == params)
                constructors.add((Constructor<T>) constructor);
        }

        return constructors;
    }

    static ReflectionFactory sunReflectImpl() {
        return ReflectAccess.REFLECTION_FACTORY;
    }
}

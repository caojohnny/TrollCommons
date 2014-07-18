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

package com.gmail.woodyc40.commons.reflection;

import com.gmail.woodyc40.commons.misc.Checks;

import java.lang.reflect.*;

/**
 * Utility methods to fetch fields and checked Reflection using {@link com.gmail.woodyc40.commons.reflection} package
 *
 * @author AgentTroll
 * @version 1.0
 * @see com.gmail.woodyc40.commons.reflection.ConstructorManager
 * @see com.gmail.woodyc40.commons.reflection.FieldManager
 * @see com.gmail.woodyc40.commons.reflection.MethodManager
 */
public final class ReflectionTool {
    private ReflectionTool() {}

    /**
     * Makes a {@code class} search for a field by name
     *
     * @param name   name of the field declared by the {@code class}
     * @param holder the {@code class} containing the field
     * @return the {@link java.lang.reflect.Field} object held by name
     */
    public static Field forField(String name, Class<?> holder) {
        Checks.notNull(name, "Cannot find null field");
        Checks.notNull(holder, "Cannot find " + name + " in class null");

        try {
            return holder.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Makes a {@code class} and all of its superclass BUT {@link java.lang.Object}.class search for a field by name
     *
     * @param name   name of the field declared by the {@code class}
     * @param holder the {@code class} containing the field
     * @return the {@link java.lang.reflect.Field} object held by name
     */
    public static Field forFieldDeep(String name, Class<?> holder) {
        Checks.notNull(name, "Cannot find null field");
        Checks.notNull(holder, "Cannot find " + name + " in class null");

        for (Class<?> current = holder;
             !current.getSuperclass().equals(Object.class);
             current = current.getSuperclass()) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Makes a {@code class} search for a method by name
     *
     * @param name   name of the method declared by the {@code class}
     * @param holder the {@code class} containing the method
     * @param params the parameters of the method
     * @return the {@link java.lang.reflect.Method} object held by name
     */
    public static Method forMethod(String name, Class<?> holder, Class<?>... params) {
        Checks.notNull(name, "Cannot find null field");
        Checks.notNull(holder, "Cannot find " + name + " in class null");
        Checks.notNull(params, "Params cannot be null, try an empty array instead");

        try {
            return holder.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Makes a {@code class} search AND all superclasses until the loop reaches {@link java.lang.Object} for a method by
     * name
     *
     * @param name   name of the method declared by the {@code class}
     * @param holder the {@code class} containing the method
     * @param params the parameters of the method
     * @return the {@link java.lang.reflect.Method} object held by name
     */
    public static Method forMethodDeep(String name, Class<?> holder, Class<?>... params) {
        Checks.notNull(name, "Cannot find null field");
        Checks.notNull(holder, "Cannot find " + name + " in class null");
        Checks.notNull(params, "Params cannot be null, try an empty array instead");

        for (Class<?> current = holder;
             !current.getSuperclass().equals(Object.class);
             current = current.getSuperclass()) {
            try {
                return current.getDeclaredMethod(name, params);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Makes a {@code class} search for a constructor by name
     *
     * @param holder the {@code class} containing the constructor
     * @return the {@link java.lang.reflect.Constructor} object held by holder
     */
    public static <T> Constructor<T> forConstruct(Class<T> holder, Class<?>... params) {
        Checks.notNull(holder, "Cannot find constructor in class null");
        Checks.notNull(params, "Params cannot be null, try an empty array instead");

        try {
            return holder.getDeclaredConstructor(params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Makes a {@code class} search AND all superclasses excluding {@link java.lang.Object} for a constructor by name
     *
     * @param holder the {@code class} containing the constructor
     * @return the {@link java.lang.reflect.Constructor} object held by holder
     */
    public static Constructor<?> forConstructDeep(Class<?> holder, Class<?>... params) {
        Checks.notNull(holder, "Cannot find constructor in class null");
        Checks.notNull(params, "Params cannot be null, try an empty array instead");

        for (Class<?> current = holder;
             !current.getSuperclass().equals(Object.class);
             current = current.getSuperclass()) {
            try {
                return current.getDeclaredConstructor(params);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}

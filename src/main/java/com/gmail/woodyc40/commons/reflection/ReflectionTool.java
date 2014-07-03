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

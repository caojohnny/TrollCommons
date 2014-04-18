/*
 * This file is part of BukkitCommons.
 *
 * Copyright (C) 2014 AgentTroll
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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

import com.gmail.woodyc40.commons.Checks;

import java.lang.reflect.Field;

/**
 * Utility methods to fetch fields and checked Reflection using {@link com.gmail.woodyc40.commons.reflection} package
 *
 * @author AgentTroll
 * @version 1.0
 * @see com.gmail.woodyc40.commons.reflection.ConstructorManager
 * @see com.gmail.woodyc40.commons.reflection.FieldManager
 * @see com.gmail.woodyc40.commons.reflection.MethodManager
 */
public class ReflectionTool {
    /**
     * Makes a <code>class</code> search for a field by name
     *
     * @param name   name of the field declared by the <code>class</code>
     * @param holder the <code>class</code> containing the field
     * @return the {@link java.lang.reflect.Field} object held by name
     * @throws NoSuchFieldException if the field is not found in the <code>class</code>
     */
    public static Field forField(String name, Class<?> holder) throws NoSuchFieldException {
        Checks.notNull(name, "Cannot find null field");
        Checks.notNull(holder, "Cannot find " + name + " in class null");

        try {
            return holder.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        throw new NoSuchFieldException("Could not find " + name + " in class " + holder.getName());
    }

    /**
     * Makes a <code>class</code> and all of its superclass BUT {@link java.lang.Object}.class search for a
     * field by name
     *
     * @param name   name of the field declared by the <code>class</code>
     * @param holder the <code>class</code> containing the field
     * @return the {@link java.lang.reflect.Field} object held by name
     * @throws NoSuchFieldException if the field is not found in the <code>class</code>
     */
    public static Field forFieldDeep(String name, Class<?> holder) throws NoSuchFieldException {
        Checks.notNull(name, "Cannot find null field");
        Checks.notNull(holder, "Cannot find " + name + " in class null");

        for (Class<?> current = holder; holder.getSuperclass()
                != Object.class; holder = holder.getSuperclass()) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                continue;
            }
        }

        throw new NoSuchFieldException("Could not find " + name + " in class " + holder.getName());
    }
}

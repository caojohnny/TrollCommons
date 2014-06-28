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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.gmail.woodyc40.commons.reflection.MethodManager;

import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

/**
 * Actual implementation of {@link com.gmail.woodyc40.commons.reflection.MethodManager} used for fast reflection
 *
 * @author AgentTroll
 * @version 1.0
 */
public class MethodImpl implements MethodManager { // TODO cache
    private final Method method;
    
    /**
     * Builds a new instance of this class by shallow method search
     *
     * @param name   the name of the declared method in the holder
     * @param holder the <code>class</code> that contains the method
     * @param params the parameter list of the method
     * TODO pull up method to ReflectionTool
     */
    public MethodImpl(String name, Class<?> holder, Object[] params) {
        try {
            this.method = holder.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException x) {
            x.printStackTrace();
        }
    }

    /**
     * Wraps the Method for management by this implementation
     *
     * @param field the Method to wrap
     */
    public MethodImpl(Method method) {
        this.method = method;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(Object inst, Object[] args) {
        ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
        MethodAccessor method = factory.newMethodAccessor(this.method); // TODO Pull up to constructor!
        try {
            method.invoke(inst, args);
        } catch (IllegalArgumentException | InvocationTargetException x) {
            x.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Method raw() {
        return this.method;
    }
}

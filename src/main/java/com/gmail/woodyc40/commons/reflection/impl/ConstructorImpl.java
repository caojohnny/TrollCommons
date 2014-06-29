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
import java.lang.reflect.Constructor;

import com.gmail.woodyc40.commons.reflection.ConstructorManager;

import sun.reflect.ConstructorAccessor;
import sun.reflect.ReflectionFactory;

/**
 * Actual implementation of {@link com.gmail.woodyc40.commons.reflection.ConstructorManager} used for fast reflection
 *
 * @author AgentTroll
 * @version 1.0
 */
public class ConstructorImpl implements ConstructorManager { // TODO cache
    private Constructor<?> constructor;
    
    /**
     * Builds a new instance of this class by shallow constructor search
     *
     * @param holder the <code>class</code> that contains the method
     * @param params the parameter list of the method
     * TODO pull up method to ReflectionTool
     */
    public ConstructorImpl(Class<?> holder, Class[] params) {
        try {
            this.constructor = holder.getDeclaredConstructor(params);
        } catch (NoSuchMethodException x) {
            x.printStackTrace();
        }
    }

    /**
     * Wraps the Consrructor for management by this implementation
     *
     * @param method the Method to wrap
     */
    public ConstructorImpl(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    /**
     * {@inheritDoc}
     */
    public Object createInstance(Object[] args) {
        ReflectionFactory factory = ReflectionFactory.getReflectionFactory(); // TODO pull up
        ConstructorAccessor method = factory.newConstructorAccessor(this.constructor); // TODO Pull up to constructor!
        try {
            return method.newInstance(args);
        } catch (IllegalArgumentException | InvocationTargetException | InstantiationException x) {
            x.printStackTrace();
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Constructor<?> raw() {
        return this.constructor;
    }
}

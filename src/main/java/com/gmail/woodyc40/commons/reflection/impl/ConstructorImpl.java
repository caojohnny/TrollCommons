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

import com.gmail.woodyc40.commons.reflection.ConstructorManager;
import sun.reflect.ConstructorAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Actual implementation of {@link com.gmail.woodyc40.commons.reflection.ConstructorManager} used for fast reflection
 *
 * @param <T> the type the constructor creates an instance of
 * @author AgentTroll
 * @version 1.0
 */
class ConstructorImpl<T> implements ConstructorManager<T> {
    private final Constructor<T>      constructor;
    private final ConstructorAccessor accessor;

    /**
     * Wraps the Consrructor for management by this implementation
     *
     * @param constructor the Constructor to wrap
     */
    public ConstructorImpl(Constructor<T> constructor) {
        this.constructor = constructor;
        this.accessor = ReflectAccess.sunReflectImpl().newConstructorAccessor(this.constructor);
    }

    /**
     * {@inheritDoc}
     */
    @Override public T createInstance(Object... args) {
        try {
            return (T) this.accessor.newInstance(args);
        } catch (IllegalArgumentException | InvocationTargetException | InstantiationException x) {
            x.printStackTrace();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override public Constructor<T> raw() {
        return this.constructor;
    }
}

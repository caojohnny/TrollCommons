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

import com.gmail.woodyc40.commons.reflection.MethodManager;
import sun.reflect.MethodAccessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Actual implementation of {@link com.gmail.woodyc40.commons.reflection.MethodManager} used for fast reflection
 *
 * @param <D> the {@code class} type declaring the method
 * @param <T> the type the method returns
 * @author AgentTroll
 * @version 1.0
 */
class MethodImpl<D, T> implements MethodManager<D, T> {
    private final Method         method;
    private final MethodAccessor accessor;

    /**
     * Wraps the Method for management by this implementation
     *
     * @param method the Method to wrap
     */
    public MethodImpl(Method method) {
        this.method = method;
        this.accessor = ReflectAccess.sunReflectImpl().newMethodAccessor(this.method);
    }

    /**
     * {@inheritDoc}
     */
    @Override public T invoke(D inst, Object... args) {
        try {
            return (T) this.accessor.invoke(inst, args);
        } catch (IllegalArgumentException | InvocationTargetException x) {
            x.printStackTrace();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override public Method raw() {
        return this.method;
    }
}

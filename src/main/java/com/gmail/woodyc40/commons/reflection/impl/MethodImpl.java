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
import sun.reflect.*;

/**
 * Actual implementation of {@link com.gmail.woodyc40.commons.reflection.MethodManager} used for fast reflection
 *
 * @author AgentTroll
 * @version 1.0
 */
public interface MethodImpl implements MethodManager { // TODO cache
    private final Method method = null;

    /**
     * {@inheritDoc}
     */
    public Object invoke(Object inst, Object[] args) {
        ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
        MethodAccessor method = factory.newMethodAccessor(method); // TODO Pull up to constructor!
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
        return method;
    }
}

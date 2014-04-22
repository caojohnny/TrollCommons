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

import java.lang.reflect.Constructor;

/**
 * <code>Interface</code> for weak access to constructor invocation methods
 *
 * <p>
 * Used to create new instances of the <code>class</code> representing the constructor
 *
 * <p>
 * Should be faster than conventional Reflection API
 *
 * @author AgentTroll
 * @version 1.0
 */
public interface ConstructorManager {
    /**
     * Creates a new instance of the <code>class</code> holding this constructor
     *
     * @param param the parameters used in the constructor instantiation
     * @return the instance of the newly instantiated <code>class</code>
     */
    public Object createInstance(Object[] param);

    /**
     * The wrapped constructor contained by this <code>class</code>
     *
     * @return the constructor that this <code>class</code> represents
     */
    public Constructor<?> raw();
}

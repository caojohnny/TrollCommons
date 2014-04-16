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

import java.lang.reflect.Method;

public interface MethodManager {
    /**
     * Calls the method
     *
     * @param inst instance of the <code>class</code> containing the method, <code>null</code> for <code>static</code>s
     * @param args arguments the pass to the method invocation
     * @return the result of the method call
     */
    public Object invoke(Object inst, Object[] args);

    /**
     * The wrapped method contained by this <code>class</code>
     *
     * @return the method that this <code>class</code> represents
     */
    public Method raw();
}

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

import java.lang.reflect.Method;

/**
 * {@code Interface} for weak access to method invocation methods
 * <p/>
 * <p/>
 * Used to invoke and {@code return} the result the method represented by this {@code class}
 * <p/>
 * <p/>
 * Should be faster than conventional Reflection API
 *
 * @param <Declaring> the {@code class} type declaring the method
 * @param <T>         the type the method returns
 * @author AgentTroll
 * @version 1.0
 */
public interface MethodManager<Declaring, T> {
    /**
     * Calls the method
     *
     * @param inst instance of the {@code class} containing the method, {@code null} for {@code static}s
     * @param args arguments the pass to the method invocation
     * @return the result of the method call
     */
    T invoke(Declaring inst, Object... args);

    /**
     * The wrapped method contained by this {@code class}
     *
     * @return the method that this {@code class} represents
     */
    Method raw();
}

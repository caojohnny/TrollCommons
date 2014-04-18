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

package com.gmail.woodyc40.commons;

/**
 * Shortcut methods to throw exceptions if condition is not met
 *
 * @author AgentTroll
 * @version 1.0
 */
public class Checks {
    private Checks() {}

    /**
     * Throws an {@link IllegalArgumentException} if the field provided is <code>null</code>.
     *
     * @param nullable the object to check not <code>null</code>
     * @param message  the message to send for the cause of the exception
     * @throws IllegalArgumentException when a nullable value is <code>null</code>
     */
    public static void notNull(Object nullable, String message) {
        if (nullable == null)
            throw new IllegalArgumentException(message);
    }

    /**
     * Throws an {@link IllegalArgumentException} objects provided are not equal to eachother.
     *
     * @param val0 the first object to check against val1
     * @param val1 the second comparison value to check against
     * @throws IllegalArgumentException when the two values do not match
     */
    public static void match(Object val0, Object val1) {
        if (val0 != val1)
            throw new IllegalArgumentException("The first value does not match the second");
    }
}

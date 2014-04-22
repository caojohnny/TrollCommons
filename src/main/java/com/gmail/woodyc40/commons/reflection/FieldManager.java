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

import java.lang.reflect.Field;

/**
 * <code>Interface</code> for weak access to field get/set methods
 *
 * <p>
 * Used to access the field that is represented by this <code>class</code>
 *
 * <p>
 * Should be faster than conventional Reflection API
 */
public interface FieldManager {
    /**
     * Sets the value of the field
     *
     * @param inst instance of the object to set. <code>null</code> for <code>static</code> fields.
     * @param val  the value to set the field as
     */
    public void set(Object inst, Object val);

    /**
     * Gets the value of the field
     *
     * @param inst instance of the object to get the field from
     * @return the value of the field set by the current instance of the holding object
     */
    public Object get(Object inst);

    /**
     * The actual {@link java.lang.reflect.Field} represented by this <code>class</code>
     *
     * @return the Field object
     */
    public Field raw();
}

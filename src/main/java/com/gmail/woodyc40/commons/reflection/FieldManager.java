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
 * {@code Interface} for weak access to field get/set methods
 * <p/>
 * <p/>
 * Used to access the field that is represented by this {@code class}
 * <p/>
 * <p/>
 * Should be faster than conventional Reflection API
 *
 * @param <Declaring> the {@code class} type declaring the field
 * @param <T>         the type the field represents
 * @author AgentTroll
 * @version 1.0
 */
public interface FieldManager<Declaring, T> {
    /**
     * Sets the value of the field
     *
     * @param inst instance of the object to set. {@code null} for {@code static} fields.
     * @param val  the value to set the field as
     */
    void set(Declaring inst, T val);

    /**
     * Gets the value of the field
     *
     * @param inst instance of the object to get the field from
     * @return the value of the field set by the current instance of the holding object
     */
    Object get(Declaring inst);

    /**
     * The actual {@link java.lang.reflect.Field} represented by this {@code class}
     *
     * @return the Field object
     */
    Field raw();
}

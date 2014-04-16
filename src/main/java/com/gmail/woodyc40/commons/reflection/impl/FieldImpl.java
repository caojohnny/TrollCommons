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

package com.gmail.woodyc40.commons.reflection.impl;

import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.gmail.woodyc40.commons.reflection.FieldManager;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;

import java.lang.reflect.Field;

class FieldImpl implements FieldManager {
    private Field field;

    /**
     * Builds a new instance of this class by shallow field search
     *
     * @param name the name of the declared field in the holder
     * @param holder the <code>class</code> that contains the field
     * @see com.gmail.woodyc40.commons.reflection.ReflectionTool#forField(String, Class)
     */
    public FieldImpl(String name, Class<?> holder) {
        try {
            this.field = ReflectionTool.forField(name, holder);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wraps the field for management by this implementation
     *
     * @param field the Field to wrap
     */
    public FieldImpl(Field field) {
        this.field = field;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final Object inst, final Object val) {
        UnsafeProvider.setField(this.field, inst, val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final Object inst) {
        return UnsafeProvider.acquireField(this.field, inst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field raw() {
        return this.field;
    }
}

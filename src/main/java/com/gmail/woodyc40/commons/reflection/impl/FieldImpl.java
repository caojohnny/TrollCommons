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

import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.gmail.woodyc40.commons.reflection.FieldManager;

import java.lang.reflect.Field;

/**
 * Actual implementation of {@link com.gmail.woodyc40.commons.reflection.FieldManager} used for fast reflection
 * <p/>
 * param <D> the {@code class} type declaring the field
 *
 * @param <T> the type the field represents
 * @author AgentTroll
 * @version 1.0
 */
class FieldImpl<D, T> implements FieldManager<D, T> {
    private final Field field;

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
    public void set(D inst, T val) {
        UnsafeProvider.setField(this.field, inst, val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(D inst) {
        return (T) UnsafeProvider.acquireField(this.field, inst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field raw() {
        return this.field;
    }
}

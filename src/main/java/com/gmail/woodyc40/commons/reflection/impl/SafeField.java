/*
 * Copyright 2014 AgentTroll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmail.woodyc40.commons.reflection.impl;

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
class SafeField<D, T> implements FieldManager<D, T> {
    private final Field field;

    /**
     * Wraps the field for management by this implementation
     *
     * @param field the Field to wrap
     */
    public SafeField(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(D inst, T val) {
        try {
            this.field.set(inst, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(D inst) {
        try {
            return (T) this.field.get(inst);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field raw() {
        return this.field;
    }
}

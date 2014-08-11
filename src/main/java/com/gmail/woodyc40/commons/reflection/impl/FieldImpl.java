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

import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.gmail.woodyc40.commons.reflection.FieldManager;

import java.lang.reflect.Field;

/**
 * Actual implementation of {@link com.gmail.woodyc40.commons.reflection.FieldManager} used for fast reflection
 * <p>
 * param <D> the {@code class} type declaring the field
 *
 * @param <D> the type of class declaring the field
 * @param <T> the type the field represents
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
class FieldImpl<D, T> implements FieldManager<D, T> {
    /** The langreflect version of the field */
    private final Field field;

    /**
     * Wraps the field for management by this implementation
     *
     * @param field the Field to wrap
     */
    public FieldImpl(Field field) {
        this.field = field;
    }

    @Override public void set(D inst, T val) {
        UnsafeProvider.setField(this.field, inst, val);
    }

    @Override public T get(D inst) {
        return (T) UnsafeProvider.acquireField(this.field, inst);
    }

    @Override public Field raw() {
        return this.field;
    }
}

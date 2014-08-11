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

package com.gmail.woodyc40.commons.concurrent;

import com.gmail.woodyc40.commons.misc.ParameterizedRunnable;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Uses an {@link java.util.concurrent.atomic.AtomicReference} to protect access to the field
 *
 * @param <T> the type of value the field protector holds
 * @author AgentTroll
 * @version 1.0
 * @since 1.1
 */
@ThreadSafe class AtomicRefProtected<T> implements ProtectedField<T> {
    /** The atomic reference to the value held by this protector */
    private final AtomicReference<T> reference;

    /**
     * Build a new protected field using an AtomicReference to access the field
     *
     * @param value the initial value of the field
     */
    public AtomicRefProtected(T value) {
        this.reference = new AtomicReference<>(value);
    }

    @Override public void access(ParameterizedRunnable<Void, T> runnable) {
        runnable.run(this.get());
    }

    @Override public T get() {
        return this.reference.get();
    }

    @Override public void set(T value) {
        this.reference.set(value);
    }
}

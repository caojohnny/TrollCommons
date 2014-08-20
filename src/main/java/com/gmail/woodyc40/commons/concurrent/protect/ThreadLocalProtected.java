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

package com.gmail.woodyc40.commons.concurrent.protect;

import com.gmail.woodyc40.commons.misc.ParameterizedRunnable;

/**
 * Protects a field value by wrapping over ThreadLocal libraries
 *
 * @param <T> the type of value the field holds
 * @author AgentTroll
 * @version 1.0
 * @since 1.1
 */
public class ThreadLocalProtected<T> implements ProtectedField<T> {
    /** The value held by the protected field. Not {@code static} since it is independent of other instances */
    private final ThreadLocal<T> value;

    /**
     * Builds a new field protector using ThreadLocal to store the field value
     *
     * @param value the initial value to set the field
     */
    public ThreadLocalProtected(final T value) {
        this.value = new ThreadLocal<T>() {
            @Override public T initialValue() {
                return value;
            }
        };
    }

    @Override public void access(ParameterizedRunnable<Void, T> runnable) {
        runnable.run(this.get());
    }

    @Override public T get() {
        return this.value.get();
    }

    @Override public void set(T value) {
        this.value.set(value);
    }
}

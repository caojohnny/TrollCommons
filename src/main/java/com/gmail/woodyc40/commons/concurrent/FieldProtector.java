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

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Factory utility used to facilitate the creation of {@link com.gmail.woodyc40.commons.concurrent.FieldProtector}s
 * <p>
 * <p>Note that protecting access to a given instance of a field does not automatically make it thread safe, only the
 * implementation and design of the underlying class with thread safety in mind will make it fully thread safe.</p>
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@Immutable @ThreadSafe
public final class FieldProtector {
    private FieldProtector() {}

    /**
     * Creates a new {@link com.gmail.woodyc40.commons.concurrent.ReentrantLockProtected} locking field protector
     *
     * @param value the initial value. Can be {@code null}.
     * @param <T>   the type of the field that is protected
     * @return the new field protector holding the initial value
     */
    public static <T> ProtectedField<T> usingReentrantLock(T value) {
        return new ReentrantLockProtected<>(value);
    }

    /**
     * Creates a new {@link com.gmail.woodyc40.commons.concurrent.AtomicRefProtected} atomic reference field protector
     *
     * @param value the initial value. Can be {@code null}.
     * @param <T>   the type of the field that is protected
     * @return the new field protector holding the initial value
     */
    public static <T> ProtectedField<T> usingAtomicRef(T value) {
        return new AtomicRefProtected<T>(value);
    }
}

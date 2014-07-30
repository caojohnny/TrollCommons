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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe class ReentrantLockProtected<T> implements ProtectedField<T> {
    /** The lock used ot guard the field */
    private final Lock lock = new ReentrantLock();
    /** The value of the field */
    private T value;

    /**
     * Constructs a new {@link java.util.concurrent.locks.ReentrantLock} based field protector
     *
     * @param value the initial value of the field
     */
    ReentrantLockProtected(T value) {
        this.set(value);
    }

    @Override public void access(ParameterizedRunnable<Void, T> runnable) {
        this.lock.lock();
        try {
            runnable.run(this.value);
        } finally {
            this.lock.unlock();
        }
    }

    @Override public T get() {
        this.lock.lock();
        try {
            return this.value;
        } finally {
            this.lock.unlock();
        }
    }

    @Override public void set(T value) {
        this.lock.lock();
        try {
            this.value = value;
        } finally {
            this.lock.unlock();
        }
    }
}

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

import com.gmail.woodyc40.commons.reflection.MethodManager;
import sun.reflect.MethodAccessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Actual implementation of {@link com.gmail.woodyc40.commons.reflection.MethodManager} used for fast reflection
 *
 * @param <D> the {@code class} type declaring the method
 * @param <T> the type the method returns
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
class MethodImpl<D, T> implements MethodManager<D, T> {
    private final Method         method;
    private final MethodAccessor accessor;

    /**
     * Wraps the Method for management by this implementation
     *
     * @param method the Method to wrap
     */
    public MethodImpl(Method method) {
        this.method = method;
        this.accessor = ReflectAccess.getREFLECTION_FACTORY().newMethodAccessor(this.method);
    }

    @Override public T invoke(D inst, Object... args) {
        try {
            return (T) this.accessor.invoke(inst, args);
        } catch (IllegalArgumentException | InvocationTargetException x) {
            x.printStackTrace();
        }

        return null;
    }

    @Override public Method raw() {
        return this.method;
    }
}

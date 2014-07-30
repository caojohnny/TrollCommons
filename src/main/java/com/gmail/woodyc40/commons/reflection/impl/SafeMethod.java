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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SafeMethod<D, T> implements MethodManager<D, T> {
    private final Method method;

    /**
     * Wraps the Method for management by this implementation
     *
     * @param method the Method to wrap
     */
    public SafeMethod(Method method) {
        this.method = method;
    }

    /**
     * {@inheritDoc}
     */
    @Override public T invoke(D inst, Object... args) {
        try {
            return (T) this.method.invoke(inst, args);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException x) {
            x.printStackTrace();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override public Method raw() {
        return this.method;
    }
}

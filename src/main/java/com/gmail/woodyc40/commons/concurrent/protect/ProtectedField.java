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

import javax.annotation.concurrent.ThreadSafe;

/**
 * Provides different methods of accessing, setting, and performing actions on fields concurrently without risk of
 * thread safety.
 *
 * @param <T> the type represented by the field protected
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@ThreadSafe
public interface ProtectedField<T> {
    /**
     * Performs a safe action on the field <p> <p>This is not truly thread safe unless the code in the
     * ParamaterizedRunnable is thread safe as well.</p>
     *
     * @param runnable {@code return null} at the end, the parameter is the injected value of the field
     */
    void access(ParameterizedRunnable<Void, T> runnable);

    /**
     * Gets the value of the field thread safely
     *
     * @return the value of the field
     */
    T get();

    /**
     * Sets the value of the field thread safely
     *
     * @param value the value to set the field to
     */
    void set(T value);
}

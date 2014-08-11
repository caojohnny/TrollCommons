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

package com.gmail.woodyc40.commons.reflection;

import java.lang.reflect.Method;

/**
 * {@code Interface} for weak access to method invocation methods
 * <p>
 * <p>Used to method and {@code return} the result the method represented by this {@code class}</p>
 * <p>
 * <p>Should be faster than conventional Reflection API</p>
 *
 * @param <Declaring> the {@code class} type declaring the method
 * @param <T>         the type the method returns
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public interface MethodManager<Declaring, T> {
    /**
     * Calls the method
     *
     * @param inst instance of the {@code class} containing the method, {@code null} for {@code static}s
     * @param args arguments the pass to the method invocation
     * @return the result of the method call
     */
    T invoke(Declaring inst, Object... args);

    /**
     * The wrapped method contained by this {@code class}
     *
     * @return the method that this {@code class} represents
     */
    Method raw();
}

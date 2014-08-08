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

import java.lang.reflect.Constructor;

/**
 * {@code Interface} for weak access to constructor invocation methods
 * <p/>
 * Used to create new instances of the {@code class} representing the constructor
 * <p/>
 * Should be faster than conventional Reflection API
 *
 * @param <T> the type the constructor creates an instance of
 * @author AgentTroll
 * @version 1.0
 */
public interface ConstructorManager<T> {
    /**
     * Creates a new instance of the {@code class} holding this constructor
     *
     * @param param the parameters used in the constructor instantiation
     * @return the instance of the newly instantiated {@code class}
     */
    T createInstance(Object... param);

    /**
     * The wrapped constructor contained by this {@code class}
     *
     * @return the constructor that this {@code class} represents
     */
    Constructor<T> raw();
}

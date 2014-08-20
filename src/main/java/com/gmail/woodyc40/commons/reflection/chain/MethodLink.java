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

package com.gmail.woodyc40.commons.reflection.chain;

import com.gmail.woodyc40.commons.reflection.MethodManager;

/**
 * Represents the method invocation link in the reflection chain
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public interface MethodLink {
    /**
     * Uses the value of a previous reflective operation to use as an instance OR params to method invocation. <p>
     * <p>The first call to this ALWAYS sets the instance</p>
     *
     * @param index the index of the object representing the instance to use or one of the parameters
     * @return the instance of MethodLink this was added to
     */
    MethodLink last(int index);

    /**
     * Appends the provided parameters, the first invocation always adding 0 index as the instance.
     *
     * @param obj the objects to use as method parameters
     * @return the instance of MethodLink this was added to
     */
    MethodLink param(Object... obj);

    /**
     * The invocation handler
     *
     * @return the means to method the method
     */
    MethodLink.Invoker invoker();

    /**
     * Adds a method to the chain
     *
     * @param name the name of the method
     * @param args the method parameters
     * @return the invoker
     */
    MethodLink method(String name, Class<?>... args);

    /**
     * Adds a method to the chain
     *
     * @param type  the return type of the method
     * @param args  the method parameter count
     * @param index the index of the method list to link the next chain
     * @return the invoker
     */
    MethodLink methodFuzzy(Class<?> type, int args, int index);

    /**
     * Adds a method to the chain
     *
     * @param type  the return type of the method
     * @param index the index of the method list to link the next chain
     * @return the invoker
     */
    MethodLink methodFuzzy(Class<?> type, int index);

    /**
     * Adds a method to the chain
     *
     * @param args  the method parameters
     * @param index the index of the method list to link the next chain
     * @return the invoker
     */
    MethodLink methodFuzzy(Class<?>[] args, int index);

    /**
     * Adds a method to the chain
     *
     * @param args  the method parameter count
     * @param index the index of the method list to link the next chain
     * @return the invoker
     */
    MethodLink invokeFuzzy(int args, int index);

    /**
     * Gets the manager used to perform the reflective actions
     *
     * @return the internal manager
     */
    MethodManager<?, ?> getManager();

    /**
     * Invokes the method and returns the next link builder
     *
     * @author AgentTroll
     * @version 1.0
     */
    interface Invoker {
        /**
         * Invokes the method
         *
         * @return the next link
         */
        ReflectionChain invoke();
    }
}

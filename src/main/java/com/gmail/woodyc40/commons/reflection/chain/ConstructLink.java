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

public interface ConstructLink {
    /**
     * Uses the class value of a previous reflective operation to use as params to construction.
     *
     * @param index the index of the object representing the parameter to use or one of the parameters
     * @return the instance of ConstructLink this was added to
     */
    ConstructLink last(int index);

    /**
     * Appends the provided parameters.
     *
     * @param obj the objects to use as construction parameters
     * @return the instance of ConstructLink this was added to
     */
    ConstructLink param(Object... obj);

    /**
     * Acquires the manager for creating new instances of the constructor's declaring class
     *
     * @return the creator access for the constructor
     */
    ConstructLink.Creator creator();

    /**
     * Sets the constructor accessor based on the parameters provided
     *
     * @param params the parameters of the constructor
     * @return the instance this was added to
     */
    ConstructLink construct(Class... params);

    /**
     * Sets the constructor accessor by index picking off of the constructor that have the provided parameter count
     *
     * @param params the number of parameters the constructor has
     * @param index  the index in the list of constructors matching the criteria
     * @return the instance this was added to
     */
    ConstructLink constructFuzzy(int params, int index);

    /**
     * The creation access for the constructor
     *
     * @author AgentTroll
     * @version 1.0
     */
    interface Creator {
        /**
         * Creates a new instance of the class declaring the constructor and pushes it to the returned ReflectionChain
         *
         * @return the ReflectionChain parenting this constructor accessor
         */
        ReflectionChain create();
    }
}

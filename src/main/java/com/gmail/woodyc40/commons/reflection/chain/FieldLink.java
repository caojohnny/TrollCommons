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

/**
 * Represents a field link in the ReflectionChain
 *
 * @author AgentTroll
 * @version 1.0
 */
public interface FieldLink {
    /**
     * Uses an instance acquired from a different reflective operation to use as the instance to perform operations on
     * the field.
     *
     * @param index the index of the returned value from an earlier reflective operation
     * @return the instance it was set to
     */
    FieldLink last(int index);

    /**
     * Uses an instance acquired from the user to perform reflective operations
     *
     * @param instance the Object instance to use
     * @return the instance it was set to
     */
    FieldLink instance(Object instance);

    /**
     * Acquires the manager to acquire the field value from
     *
     * @return the getter of the field
     */
    FieldLink.Getter getter();

    /**
     * Acquires the mananger to push the value of the field to
     * 
     * @return the setter of the field
     */
    FieldLink.Setter setter();

    /**
     * Sets the field accessor for the field name provided
     *
     * @param name the name of the field to be accessed
     * @return the instance it was set to
     */
    FieldLink field(String name);

    /**
     * Sets the field accessor using "fuzzy" search
     *
     * @param type the type the field represents
     * @param index the index to use from a List of fields following the criteria
     * @return the instance it was set to
     */
    FieldLink fieldFuzzy(Class<?> type, int index);

    /**
     * The field getter access
     *
     * @author AgentTroll
     * @version 1.0
     */
    interface Getter {
        /**
         * Pushes the value of the field to the last index place in returned instances in ReflectionChain
         *
         * @return the instance the instance was added to
         */
        ReflectionChain get();
    }

    /**
     * The field setter access
     *
     * @author AgentTroll
     * @version 1.0
     */
    interface Setter {
        /**
         * Sets the value of the field. This technically doesn't return anything, and does not push a new value
         * to the returned value list in ReflectionChain, only setting the field.
         *
         * @param value the value to set the field
         * @return the instance of the ReflectionChain parenting the wrapping FieldLink
         */
        ReflectionChain set(Object value);
    }
}

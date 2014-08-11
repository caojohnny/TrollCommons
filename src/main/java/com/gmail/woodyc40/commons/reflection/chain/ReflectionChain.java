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

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * The base builder for the reflection chain
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
public class ReflectionChain {
    /** The values found via reflection in the chain */
    final List<Object> returned = new ArrayList<>();
    /** The starting class value */
    private final Class<?> base;

    /**
     * Gets the last reflected object
     *
     * @return the last object to be found via reflection
     */
    public Object reflect() { return this.returned.get(this.returned.size() - 1); }

    /**
     * Generates a new method link that provides access to method reflection for this chain
     *
     * @return the new method link associated to this chain
     */
    public MethodLink method() {
        return new MethodLinkImpl(this.base, this);
    }

    /**
     * Custom method accessor that doesn't depend on the base class from the chain constructor
     *
     * @param base the class to use instead
     * @return the new method link associated with this chain
     */
    public MethodLink method(Class<?> base) {
        return new MethodLinkImpl(base, this);
    }

    /**
     * The field accessor that provides access for field reflection in this chain
     *
     * @return the new field link associated with the chain
     */
    public FieldLink field() { return new FieldLinkImpl(this.base, this); }

    /**
     * Custom field accessor that doesn't depend on the base class from the chain constructor
     *
     * @param base the class to use instead
     * @return the new field link associated with the chain
     */
    public FieldLink field(Class<?> base) { return new FieldLinkImpl(base, this); }

    /**
     * Generates a new constructor accessor that provides access for construction for reflection based on this chain
     *
     * @return the new constructor link associated with this chain
     */
    public ConstructLink contruct() { return new ConstructLinkImpl(this.base, this); }

    /**
     * Custom constructor accessor that doesn't depend on the base class from the chain constructor
     *
     * @param base the class to use instead
     * @return the new constructor link associated with this chain
     */
    public ConstructLink contruct(Class<?> base) { return new ConstructLinkImpl(base, this); }
}

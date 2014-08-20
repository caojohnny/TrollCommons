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

package com.gmail.woodyc40.commons.instrument.experimental;

/**
 * Thrown when the constant type cannot be determined in the constant pool <p> <p>Appears in the format: Could not find
 * constant at index (CP index) in class (Class name)</p>
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public class ConstantNotFoundException extends Exception {
    private static final long serialVersionUID = 1524021269321344472L;

    /**
     * Constructs this exception
     *
     * @param clazz the class where the constant type cannot be determined
     * @param i     debugging purpose integer, the index of the constant
     */
    public ConstantNotFoundException(Class<?> clazz, int i) {
        super("Could not find constant at index " + i + " in class " + clazz.getName());
    }
}

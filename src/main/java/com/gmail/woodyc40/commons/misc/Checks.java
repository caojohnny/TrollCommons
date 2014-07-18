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

package com.gmail.woodyc40.commons.misc;

/**
 * Shortcut methods to throw exceptions if condition is not met
 *
 * @author AgentTroll
 * @version 1.0
 */
public final class Checks {
    private Checks() {}

    /**
     * Throws an {@link IllegalArgumentException} if the field provided is {@code null}.
     *
     * @param nullable the object to check not {@code null}
     * @param message  the message to send for the cause of the exception
     * @throws IllegalArgumentException when a nullable value is {@code null}
     */
    public static void notNull(Object nullable, String message) {
        if (nullable == null)
            throw new IllegalArgumentException(message);
    }

    /**
     * Throws an {@link IllegalArgumentException} objects provided are not equal to eachother.
     *
     * @param val0 the first object to check against val1
     * @param val1 the second comparison value to check against
     * @throws IllegalArgumentException when the two values do not match
     */
    public static void match(Object val0, Object val1) {
        if (!val0.equals(val1))
            throw new IllegalArgumentException("The first value does not match the second");
    }
}

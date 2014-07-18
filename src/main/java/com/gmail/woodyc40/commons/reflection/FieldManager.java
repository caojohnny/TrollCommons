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

import java.lang.reflect.Field;

/**
 * {@code Interface} for weak access to field get/set methods
 * <p/>
 * <p/>
 * Used to access the field that is represented by this {@code class}
 * <p/>
 * <p/>
 * Should be faster than conventional Reflection API
 *
 * @param <Declaring> the {@code class} type declaring the field
 * @param <T>         the type the field represents
 * @author AgentTroll
 * @version 1.0
 */
public interface FieldManager<Declaring, T> {
    /**
     * Sets the value of the field
     *
     * @param inst instance of the object to set. {@code null} for {@code static} fields.
     * @param val  the value to set the field as
     */
    void set(Declaring inst, T val);

    /**
     * Gets the value of the field
     *
     * @param inst instance of the object to get the field from
     * @return the value of the field set by the current instance of the holding object
     */
    Object get(Declaring inst);

    /**
     * The actual {@link java.lang.reflect.Field} represented by this {@code class}
     *
     * @return the Field object
     */
    Field raw();
}

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

import java.io.Serializable;

/**
 * A runnable that can be serialized. Useful for RMI or transport of execution sequences. <p> <p>Objects used in the
 * code even of a serializable runnable must be serializable as well</p> <p> <p>Implementing this class requires you to
 * define a serialVersionUID for optimal results</p>
 *
 * @param <V> the return type
 * @author AgentTroll
 * @version 1.0
 * @see java.io.Serializable
 * @see java.lang.Runnable
 * @since 1.0
 */
public interface SerializableRunnable<V> extends Serializable {
    /**
     * Runs the abstraction
     *
     * @return the value returned by implementations
     */
    V run();
}

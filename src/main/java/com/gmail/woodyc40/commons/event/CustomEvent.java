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

package com.gmail.woodyc40.commons.event;

import javax.annotation.concurrent.ThreadSafe;

/**
 * The standard type used to represent an Event called in the framework <p> <p>All implementations of this class are
 * expected to be thread safe by design.</p> <p> <p>All implementations of this class are also expected to have a {@link
 * com.gmail.woodyc40.commons.event.Handler} annotation</p>
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@ThreadSafe
public interface CustomEvent {
    /**
     * If the event is cancelled
     *
     * @return {@code true} if the event is cancelled, {@code false} if it isn't.
     */
    boolean getCancelled();

    /**
     * Sets the event cancel state
     *
     * @param cancelled {@code true} to cancel the event, {@code false} to continue it
     */
    void setCancelled(boolean cancelled);
}

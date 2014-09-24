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

import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Enumeration representing all of the types of event that can be called <p> <p>This is needed to separate different
 * events and make sure the types are the same</p>
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@ThreadSafe // :)
public enum EventType {
    ;

    /** The {@code class} that the event is represented by */
    @Getter private final Class<? extends CustomEvent> eventClass;

    /**
     * Constructs a new entry for the event type specified
     *
     * @param eventClass the {@code class} that represents the event type
     */
    EventType(Class<? extends CustomEvent> eventClass) {
        this.eventClass = eventClass;
    }
}

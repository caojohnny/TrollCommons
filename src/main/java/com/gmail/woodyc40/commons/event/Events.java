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

import org.bukkit.Bukkit;

import javax.annotation.concurrent.GuardedBy;
import java.util.HashMap;
import java.util.Map;

public final class Events {
    /** The registered listeners */
    @GuardedBy("Events.class") private static final Map<EventHandler, EventType> registered = new HashMap<>();

    /**
     * No instantiation
     */
    private Events() {}

    /**
     * Asynchronously register a handler
     *
     * @param handler the listener to register
     * @throws NoEventAnnotationException when the handler is not annotated with a Handler annotation
     */
    public static void register(EventHandler handler) throws NoEventAnnotationException {
        if (handler.getClass().getAnnotation(Handler.class) == null)
            throw new NoEventAnnotationException(handler.getClass());

        if (Bukkit.isPrimaryThread()) {
            Events.registered.put(handler, handler.getClass().getAnnotation(Handler.class).value());
            return;
        }

        synchronized (Events.class) {
            Events.registered.put(handler, handler.getClass().getAnnotation(Handler.class).value());
        }
    }

    /**
     * Asynchronously call the event, firing all of the listeners
     *
     * @param event the event to call
     */
    public static void call(CustomEvent event) {
        if (Bukkit.isPrimaryThread()) {
            for (Map.Entry<EventHandler, EventType> entry : Events.registered.entrySet())
                if (entry.getValue() == event.getClass().getAnnotation(Handler.class).value())
                    entry.getKey().handle(event);

            return;
        }

        synchronized (Events.class) {
            for (Map.Entry<EventHandler, EventType> entry : Events.registered.entrySet())
                if (entry.getValue() == event.getClass().getAnnotation(Handler.class).value())
                    entry.getKey().handle(event);
        }
    }
}
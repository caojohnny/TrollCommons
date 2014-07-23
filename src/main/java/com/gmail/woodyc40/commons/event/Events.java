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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Events {
    private static final Map<EventHandler, EventType> registered = new ConcurrentHashMap<>();

    private Events() {}

    public static void register(EventHandler handler) throws NoEventAnnotationException {
        if (handler.getClass().getAnnotation(Handler.class) == null)
            throw new NoEventAnnotationException(handler.getClass());

        Events.registered.put(handler, handler.getClass().getAnnotation(Handler.class).value());
    }

    public static void call(CustomEvent event) {
        for (Map.Entry<EventHandler, EventType> entry : Events.registered.entrySet())
            if (entry.getValue() == event.getClass().getAnnotation(Handler.class).value())
                entry.getKey().handle(event);
    }
}
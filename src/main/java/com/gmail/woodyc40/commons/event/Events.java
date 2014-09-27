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

import com.gmail.woodyc40.commons.Commons;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The events utility for calling and registering their respective events through an asynchronous environment
 *
 * @author AgentTroll
 * @version 2.0
 * @since 1.0
 */
@ThreadSafe
public final class Events {
    /** The registered listeners */
    @GuardedBy("Events.class") private static final Map<EventHandler, EventType> registered = new HashMap<>();
    /** The event thread */
    private static final                            Events.EventThread           THREAD     = new Events.EventThread();

    static {
        Events.THREAD.start();
    }

    private Events() {} // Suppress instantiation

    /**
     * Asynchronously register a handler
     *
     * @param handler the listener to register
     * @throws NoEventAnnotationException when the handler is not annotated with a Handler annotation
     */
    public static void register(EventHandler handler) throws NoEventAnnotationException {
        if (handler.getClass().getAnnotation(Handler.class) == null)
            throw new NoEventAnnotationException(handler.getClass());

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
        Events.THREAD.call(event);
    }

    /**
     * Stops the events from being registered and stops the event loop <p> <p>You should never ever call this.
     * Nevertheless, this cannot be called by you anyways.</p>
     */
    public static void shutdown() throws SecurityException {
        Package settings = Commons.getCaller(false);
        if (settings == null)
            throw new SecurityException("ACCESS DENIED: Not called from main class");
        if (!settings.equals(Commons.class.getPackage()))
            throw new SecurityException("ACCESS DENIED: You are not TrollCommons");
        Events.THREAD.interrupt();
    }

    /**
     * The handler thread wrapper that calls and registers events in the event loop
     *
     * @author AgentTroll
     * @version 1.0
     * @since 1.1
     */
    private static class EventThread extends Thread {
        /** The execution tasks */
        private final Queue<Runnable> executor = new LinkedBlockingDeque<>();
        /** If the thread is running, or naw */
        private volatile boolean running;

        /**
         * Does the handling for the event call
         *
         * @param event the event to call
         */
        private static void handleEvent(CustomEvent event) {
            synchronized (Events.class) {
                for (Map.Entry<EventHandler, EventType> entry : Events.registered.entrySet())
                    if (entry.getValue() == event.getClass().getAnnotation(Handler.class).value())
                        entry.getKey().handle(event);
            }
        }

        @Override public synchronized void start() {
            super.start();
            this.running = true;
        }

        @Override public void run() {
            while (this.running) {
                Runnable runnable = this.executor.poll();
                if (runnable == null) continue;
                runnable.run();
            }
        }

        @Override public void interrupt() {
            super.interrupt();
            this.running = false;
        }

        /**
         * Signals the thread to handle the available event listeners
         *
         * @param event the event to call
         */
        public void call(final CustomEvent event) {
            this.executor.add(new Runnable() {
                @Override public void run() {
                    Events.EventThread.handleEvent(event);
                }
            });
        }
    }
}
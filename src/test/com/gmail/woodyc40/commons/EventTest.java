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

package com.gmail.woodyc40.commons;

import com.gmail.woodyc40.commons.concurrent.RunnableReceiveEvent;
import com.gmail.woodyc40.commons.event.*;
import com.gmail.woodyc40.commons.misc.SerializableRunnable;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class EventTest {
    private static final EventHandler LISTENER = new EventTest.Listener();
    private static final CustomEvent  EVENT    = new RunnableReceiveEvent(new SerializableRunnable<Object>() {
        private static final long serialVersionUID = 6131134329231886819L;

        @Override public Object run() {
            return null;
        }
    });

    @Benchmark public static void aRegister() throws NoEventAnnotationException {
        Events.register(EventTest.LISTENER);
    }

    @Benchmark public static void bCall() {
        Events.call(EventTest.EVENT);
    }

    //@Benchmark public static void aRegisterSync() throws NoEventAnnotationException {
    //    Events.registerSync(EventTest.LISTENER);
    //}

    //@Benchmark public static void bCallSync() {
    //    Events.callSync(EventTest.EVENT);
    //}

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + EventTest.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Handler(EventType.RUNNABLE_RECEIVE)
    private static class Listener implements EventHandler {
        @Override public void handle(CustomEvent event) {
            event.setCancelled(true);
        }
    }
}

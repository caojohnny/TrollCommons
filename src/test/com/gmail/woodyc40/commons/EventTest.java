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

/*
# Run progress: 0.00% complete, ETA 00:00:40
# Warmup: 10 iterations, 1 s each
# Measurement: 10 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.EventTest.aRegister
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 173.143 ns/op
# Warmup Iteration   2: 147.825 ns/op
# Warmup Iteration   3: 143.603 ns/op
# Warmup Iteration   4: 141.272 ns/op
# Warmup Iteration   5: 122.546 ns/op
# Warmup Iteration   6: 107.430 ns/op
# Warmup Iteration   7: 142.489 ns/op
# Warmup Iteration   8: 154.978 ns/op
# Warmup Iteration   9: 133.859 ns/op
# Warmup Iteration  10: 107.272 ns/op
Iteration   1: 106.469 ns/op
Iteration   2: 118.522 ns/op
Iteration   3: 119.092 ns/op
Iteration   4: 129.843 ns/op
Iteration   5: 137.091 ns/op
Iteration   6: 140.330 ns/op
Iteration   7: 127.272 ns/op
Iteration   8: 115.639 ns/op
Iteration   9: 116.953 ns/op
Iteration  10: 120.886 ns/op

Result: 123.210 ±(99.9%) 15.674 ns/op [Average]
  Statistics: (min, avg, max) = (106.469, 123.210, 140.330), stdev = 10.367
  Confidence interval (99.9%): [107.536, 138.884]


# Run progress: 50.00% complete, ETA 00:00:24
# Warmup: 10 iterations, 1 s each
# Measurement: 10 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.EventTest.bCall
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 274.368 ns/op
# Warmup Iteration   2: 413.320 ns/op
# Warmup Iteration   3: 177.137 ns/op
# Warmup Iteration   4: 199.558 ns/op
# Warmup Iteration   5: 472.532 ns/op
# Warmup Iteration   6: 189.899 ns/op
# Warmup Iteration   7: 579.520 ns/op
# Warmup Iteration   8: 245.338 ns/op
# Warmup Iteration   9: 539.042 ns/op
# Warmup Iteration  10: 239.969 ns/op
Iteration   1: 241.162 ns/op
Iteration   2: 423.774 ns/op
Iteration   3: 310.272 ns/op
Iteration   4: 272.852 ns/op
Iteration   5: 215.050 ns/op
Iteration   6: 251.155 ns/op
Iteration   7: 302.220 ns/op
Iteration   8: 301.112 ns/op
Iteration   9: 121.661 ns/op
Iteration  10: 111.234 ns/op

Result: 255.049 ±(99.9%) 139.354 ns/op [Average]
  Statistics: (min, avg, max) = (111.234, 255.049, 423.774), stdev = 92.174
  Confidence interval (99.9%): [115.695, 394.403]


# Run complete. Total time: 00:00:56

Benchmark                       Mode   Samples        Score  Score error    Units
c.g.w.c.EventTest.aRegister     avgt        10      123.210       15.674    ns/op
c.g.w.c.EventTest.bCall         avgt        10      255.049      139.354    ns/op
 */
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

    public static void main(String... args) throws RunnerException, NoEventAnnotationException {
        Options opt = new OptionsBuilder()
                .include(".*" + EventTest.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(10)
                .measurementIterations(10)
                .forks(1)
                .build();

        new Runner(opt).run();
        //Events.register(EventTest.LISTENER);
        //Events.call(EventTest.EVENT);
        //Events.shutdown();
    }

    @Handler(EventType.RUNNABLE_RECEIVE)
    private static class Listener implements EventHandler {
        @Override public void handle(CustomEvent event) {
            event.setCancelled(true);
            System.out.println("Event received, return value: " + ((RunnableReceiveEvent) event).getRunnable().run());
        }
    }
}

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

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

/*
# Run progress: 0.00% complete, ETA 00:01:40
# Warmup: 25 iterations, 1 s each
# Measurement: 25 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SynchronizationTest.synchSynchedMap
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7533 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 35.441 ns/op
# Warmup Iteration   2: 34.078 ns/op
# Warmup Iteration   3: 34.306 ns/op
# Warmup Iteration   4: 34.312 ns/op
# Warmup Iteration   5: 34.440 ns/op
# Warmup Iteration   6: 34.706 ns/op
# Warmup Iteration   7: 34.484 ns/op
# Warmup Iteration   8: 34.275 ns/op
# Warmup Iteration   9: 34.478 ns/op
# Warmup Iteration  10: 34.680 ns/op
# Warmup Iteration  11: 34.799 ns/op
# Warmup Iteration  12: 34.876 ns/op
# Warmup Iteration  13: 34.499 ns/op
# Warmup Iteration  14: 34.436 ns/op
# Warmup Iteration  15: 34.497 ns/op
# Warmup Iteration  16: 34.481 ns/op
# Warmup Iteration  17: 34.466 ns/op
# Warmup Iteration  18: 34.465 ns/op
# Warmup Iteration  19: 34.424 ns/op
# Warmup Iteration  20: 34.470 ns/op
# Warmup Iteration  21: 34.515 ns/op
# Warmup Iteration  22: 35.105 ns/op
# Warmup Iteration  23: 34.430 ns/op
# Warmup Iteration  24: 34.546 ns/op
# Warmup Iteration  25: 35.112 ns/op
Iteration   1: 34.655 ns/op
Iteration   2: 34.644 ns/op
Iteration   3: 34.637 ns/op
Iteration   4: 34.661 ns/op
Iteration   5: 34.484 ns/op
Iteration   6: 34.594 ns/op
Iteration   7: 34.619 ns/op
Iteration   8: 34.440 ns/op
Iteration   9: 34.395 ns/op
Iteration  10: 34.409 ns/op
Iteration  11: 34.364 ns/op
Iteration  12: 34.370 ns/op
Iteration  13: 34.367 ns/op
Iteration  14: 34.593 ns/op
Iteration  15: 34.334 ns/op
Iteration  16: 34.440 ns/op
Iteration  17: 34.420 ns/op
Iteration  18: 34.415 ns/op
Iteration  19: 34.513 ns/op
Iteration  20: 34.293 ns/op
Iteration  21: 34.458 ns/op
Iteration  22: 34.449 ns/op
Iteration  23: 34.498 ns/op
Iteration  24: 34.278 ns/op
Iteration  25: 34.592 ns/op

Result: 34.477 ±(99.9%) 0.088 ns/op [Average]
  Statistics: (min, avg, max) = (34.278, 34.477, 34.661), stdev = 0.118
  Confidence interval (99.9%): [34.389, 34.565]


# Run progress: 50.00% complete, ETA 00:01:00
# Warmup: 25 iterations, 1 s each
# Measurement: 25 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SynchronizationTest.synchedMap
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7533 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 22.228 ns/op
# Warmup Iteration   2: 22.102 ns/op
# Warmup Iteration   3: 21.882 ns/op
# Warmup Iteration   4: 21.888 ns/op
# Warmup Iteration   5: 21.868 ns/op
# Warmup Iteration   6: 22.136 ns/op
# Warmup Iteration   7: 22.102 ns/op
# Warmup Iteration   8: 21.955 ns/op
# Warmup Iteration   9: 22.102 ns/op
# Warmup Iteration  10: 22.101 ns/op
# Warmup Iteration  11: 22.122 ns/op
# Warmup Iteration  12: 22.063 ns/op
# Warmup Iteration  13: 22.018 ns/op
# Warmup Iteration  14: 22.111 ns/op
# Warmup Iteration  15: 22.100 ns/op
# Warmup Iteration  16: 22.119 ns/op
# Warmup Iteration  17: 22.095 ns/op
# Warmup Iteration  18: 22.085 ns/op
# Warmup Iteration  19: 22.094 ns/op
# Warmup Iteration  20: 22.103 ns/op
# Warmup Iteration  21: 22.106 ns/op
# Warmup Iteration  22: 22.114 ns/op
# Warmup Iteration  23: 21.961 ns/op
# Warmup Iteration  24: 21.938 ns/op
# Warmup Iteration  25: 21.890 ns/op
Iteration   1: 21.934 ns/op
Iteration   2: 21.950 ns/op
Iteration   3: 21.962 ns/op
Iteration   4: 22.062 ns/op
Iteration   5: 21.906 ns/op
Iteration   6: 22.004 ns/op
Iteration   7: 21.906 ns/op
Iteration   8: 21.883 ns/op
Iteration   9: 22.176 ns/op
Iteration  10: 22.042 ns/op
Iteration  11: 21.976 ns/op
Iteration  12: 21.882 ns/op
Iteration  13: 22.090 ns/op
Iteration  14: 21.917 ns/op
Iteration  15: 21.952 ns/op
Iteration  16: 22.194 ns/op
Iteration  17: 21.992 ns/op
Iteration  18: 21.959 ns/op
Iteration  19: 22.269 ns/op
Iteration  20: 22.276 ns/op
Iteration  21: 22.207 ns/op
Iteration  22: 22.191 ns/op
Iteration  23: 22.247 ns/op
Iteration  24: 22.074 ns/op
Iteration  25: 21.951 ns/op

Result: 22.040 ±(99.9%) 0.097 ns/op [Average]
  Statistics: (min, avg, max) = (21.882, 22.040, 22.276), stdev = 0.130
  Confidence interval (99.9%): [21.943, 22.137]


# Run complete. Total time: 00:02:01

Benchmark                                       Mode   Samples        Score  Score error    Units
c.g.w.c.SynchronizationTest.synchSynchedMap     avgt        25       34.477        0.088    ns/op
c.g.w.c.SynchronizationTest.synchedMap          avgt        25       22.040        0.097    ns/op
 */

@State(Scope.Benchmark)
public class SynchronizationTest {
    private static final Map<Object, Object> MAP   = Collections.synchronizedMap(new HashMap<>());
    private static final Object              KEY   = new Object();
    private static final Object              VALUE = new Object();

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .forks(1)
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .include(".*" + SynchronizationTest.class.getSimpleName() + ".*")
                .warmupIterations(25)
                .measurementIterations(25)
                .build();

        new Runner(opt).run();
    }

    @Setup public void setup() {
        SynchronizationTest.MAP.put(SynchronizationTest.KEY, SynchronizationTest.VALUE);
    }

    @Benchmark public void synchedMap() {
        SynchronizationTest.MAP.get(SynchronizationTest.KEY);
    }

    @Benchmark public void synchSynchedMap() {
        synchronized (SynchronizationTest.MAP) {
            SynchronizationTest.MAP.get(SynchronizationTest.KEY);
        }
    }
}

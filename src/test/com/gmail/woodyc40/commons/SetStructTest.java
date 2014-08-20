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

import com.gmail.woodyc40.commons.collect.AbstractHashStruct;
import com.gmail.woodyc40.commons.collect.StructBuilder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

/*
# Run progress: 0.00% complete, ETA 00:01:20
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SetStructTest.testInsertion
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 7.500 ns/op
# Warmup Iteration   2: 7.321 ns/op
# Warmup Iteration   3: 10.011 ns/op
# Warmup Iteration   4: 9.345 ns/op
# Warmup Iteration   5: 8.538 ns/op
Iteration   1: 8.649 ns/op
Iteration   2: 8.649 ns/op
Iteration   3: 8.613 ns/op
Iteration   4: 8.707 ns/op
Iteration   5: 8.617 ns/op

Result: 8.647 ±(99.9%) 0.144 ns/op [Average]
  Statistics: (min, avg, max) = (8.613, 8.647, 8.707), stdev = 0.037
  Confidence interval (99.9%): [8.503, 8.791]


# Run progress: 12.50% complete, ETA 00:01:27
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SetStructTest.testInsertion
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 7.339 ns/op
# Warmup Iteration   2: 7.115 ns/op
# Warmup Iteration   3: 10.032 ns/op
# Warmup Iteration   4: 9.350 ns/op
# Warmup Iteration   5: 8.731 ns/op
Iteration   1: 8.582 ns/op
Iteration   2: 8.696 ns/op
Iteration   3: 8.670 ns/op
Iteration   4: 8.783 ns/op
Iteration   5: 8.648 ns/op

Result: 8.676 ±(99.9%) 0.283 ns/op [Average]
  Statistics: (min, avg, max) = (8.582, 8.676, 8.783), stdev = 0.073
  Confidence interval (99.9%): [8.393, 8.959]


# Run progress: 25.00% complete, ETA 00:01:14
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SetStructTest.testInsertion0
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 5.636 ns/op
# Warmup Iteration   2: 5.474 ns/op
# Warmup Iteration   3: 5.385 ns/op
# Warmup Iteration   4: 5.358 ns/op
# Warmup Iteration   5: 5.458 ns/op
Iteration   1: 5.421 ns/op
Iteration   2: 5.354 ns/op
Iteration   3: 5.390 ns/op
Iteration   4: 5.376 ns/op
Iteration   5: 5.450 ns/op

Result: 5.398 ±(99.9%) 0.145 ns/op [Average]
  Statistics: (min, avg, max) = (5.354, 5.398, 5.450), stdev = 0.038
  Confidence interval (99.9%): [5.254, 5.543]


# Run progress: 37.50% complete, ETA 00:01:02
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SetStructTest.testInsertion0
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 5.812 ns/op
# Warmup Iteration   2: 5.400 ns/op
# Warmup Iteration   3: 5.452 ns/op
# Warmup Iteration   4: 5.416 ns/op
# Warmup Iteration   5: 5.375 ns/op
Iteration   1: 5.487 ns/op
Iteration   2: 5.507 ns/op
Iteration   3: 5.488 ns/op
Iteration   4: 5.361 ns/op
Iteration   5: 5.409 ns/op

Result: 5.450 ±(99.9%) 0.241 ns/op [Average]
  Statistics: (min, avg, max) = (5.361, 5.450, 5.507), stdev = 0.063
  Confidence interval (99.9%): [5.210, 5.691]


# Run progress: 50.00% complete, ETA 00:00:49
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SetStructTest.testRemoval
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 9.588 ns/op
# Warmup Iteration   2: 8.857 ns/op
# Warmup Iteration   3: 6.718 ns/op
# Warmup Iteration   4: 6.838 ns/op
# Warmup Iteration   5: 6.701 ns/op
Iteration   1: 6.677 ns/op
Iteration   2: 6.702 ns/op
Iteration   3: 6.805 ns/op
Iteration   4: 6.705 ns/op
Iteration   5: 6.774 ns/op

Result: 6.733 ±(99.9%) 0.209 ns/op [Average]
  Statistics: (min, avg, max) = (6.677, 6.733, 6.805), stdev = 0.054
  Confidence interval (99.9%): [6.524, 6.941]


# Run progress: 62.50% complete, ETA 00:00:37
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SetStructTest.testRemoval
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 10.156 ns/op
# Warmup Iteration   2: 9.004 ns/op
# Warmup Iteration   3: 6.764 ns/op
# Warmup Iteration   4: 6.699 ns/op
# Warmup Iteration   5: 6.684 ns/op
Iteration   1: 6.743 ns/op
Iteration   2: 6.776 ns/op
Iteration   3: 6.822 ns/op
Iteration   4: 6.805 ns/op
Iteration   5: 6.734 ns/op

Result: 6.776 ±(99.9%) 0.147 ns/op [Average]
  Statistics: (min, avg, max) = (6.734, 6.776, 6.822), stdev = 0.038
  Confidence interval (99.9%): [6.629, 6.923]


# Run progress: 75.00% complete, ETA 00:00:24
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SetStructTest.testRemoval0
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 5.596 ns/op
# Warmup Iteration   2: 5.463 ns/op
# Warmup Iteration   3: 4.595 ns/op
# Warmup Iteration   4: 4.621 ns/op
# Warmup Iteration   5: 4.722 ns/op
Iteration   1: 4.726 ns/op
Iteration   2: 4.716 ns/op
Iteration   3: 4.725 ns/op
Iteration   4: 4.755 ns/op
Iteration   5: 4.753 ns/op

Result: 4.735 ±(99.9%) 0.069 ns/op [Average]
  Statistics: (min, avg, max) = (4.716, 4.735, 4.755), stdev = 0.018
  Confidence interval (99.9%): [4.666, 4.804]


# Run progress: 87.50% complete, ETA 00:00:12
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.SetStructTest.testRemoval0
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 5.467 ns/op
# Warmup Iteration   2: 5.311 ns/op
# Warmup Iteration   3: 4.810 ns/op
# Warmup Iteration   4: 4.762 ns/op
# Warmup Iteration   5: 4.750 ns/op
Iteration   1: 4.671 ns/op
Iteration   2: 4.654 ns/op
Iteration   3: 4.662 ns/op
Iteration   4: 4.736 ns/op
Iteration   5: 4.669 ns/op

Result: 4.679 ±(99.9%) 0.127 ns/op [Average]
  Statistics: (min, avg, max) = (4.654, 4.679, 4.736), stdev = 0.033
  Confidence interval (99.9%): [4.552, 4.805]


# Run complete. Total time: 00:01:39

Benchmark                              (entries)   Mode   Samples        Score  Score error    Units
c.g.w.c.SetStructTest.testInsertion           10   avgt         5        8.647        0.144    ns/op
c.g.w.c.SetStructTest.testInsertion         1000   avgt         5        8.676        0.283    ns/op
c.g.w.c.SetStructTest.testInsertion0          10   avgt         5        5.398        0.145    ns/op
c.g.w.c.SetStructTest.testInsertion0        1000   avgt         5        5.450        0.241    ns/op
c.g.w.c.SetStructTest.testRemoval             10   avgt         5        6.733        0.209    ns/op
c.g.w.c.SetStructTest.testRemoval           1000   avgt         5        6.776        0.147    ns/op
c.g.w.c.SetStructTest.testRemoval0            10   avgt         5        4.735        0.069    ns/op
c.g.w.c.SetStructTest.testRemoval0          1000   avgt         5        4.679        0.127    ns/op
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SetStructTest {
    private static final int                 add  = 1249012735;
    private static       Set<Integer>        map0 = new HashSet<>();
    private static       Collection<Integer> map  = new HashSet<>();
    @Param({ "10", "1000" }) private int entries;

    public static void main(String... args) throws RunnerException {
        SetStructTest.run();
    }

    public static void run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .forks(1)
                .include(".*" + SetStructTest.class.getSimpleName() + ".*")
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }

    @Setup public void setUp() {
        SetStructTest.map = new HashSet<>(this.entries);
        SetStructTest.map0 = new StructBuilder().hash(AbstractHashStruct.HashStrategy.JAVA).buildSet();

        for (int i = 0; i <= this.entries; i++) {
            if (i == 69) {
                SetStructTest.map.add(69);
                SetStructTest.map0.add(69);

                continue;
            }
            SetStructTest.map.add(i);
            SetStructTest.map0.add(i);
        }
    }

    @Benchmark public void testInsertion() {
        SetStructTest.map.add(SetStructTest.add);
    }

    @Benchmark public void testInsertion0() {
        SetStructTest.map0.add(SetStructTest.add);
    }

    @Benchmark public void testRemoval() {
        SetStructTest.map.remove(SetStructTest.add);
    }

    @Benchmark public void testRemoval0() {
        SetStructTest.map0.remove(SetStructTest.add);
    }
}

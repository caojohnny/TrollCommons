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
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/*

Output:
# Run progress: 0.00% complete, ETA 00:01:00
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.atroll
# Parameters: (size = 6999)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 31.737 ns/op
# Warmup Iteration   2: 25.976 ns/op
# Warmup Iteration   3: 21.888 ns/op
# Warmup Iteration   4: 21.886 ns/op
# Warmup Iteration   5: 21.314 ns/op
Iteration   1: 21.867 ns/op
Iteration   2: 21.264 ns/op
Iteration   3: 21.438 ns/op
Iteration   4: 21.060 ns/op
Iteration   5: 21.462 ns/op

Result: 21.418 ±(99.9%) 1.148 ns/op [Average]
  Statistics: (min, avg, max) = (21.060, 21.418, 21.867), stdev = 0.298
  Confidence interval (99.9%): [20.270, 22.566]


# Run progress: 16.67% complete, ETA 00:01:08
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.atroll
# Parameters: (size = 16)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 24.568 ns/op
# Warmup Iteration   2: 22.338 ns/op
# Warmup Iteration   3: 22.006 ns/op
# Warmup Iteration   4: 21.099 ns/op
# Warmup Iteration   5: 21.216 ns/op
Iteration   1: 21.831 ns/op
Iteration   2: 21.239 ns/op
Iteration   3: 21.077 ns/op
Iteration   4: 21.129 ns/op
Iteration   5: 21.130 ns/op

Result: 21.281 ±(99.9%) 1.205 ns/op [Average]
  Statistics: (min, avg, max) = (21.077, 21.281, 21.831), stdev = 0.313
  Confidence interval (99.9%): [20.076, 22.486]


# Run progress: 33.33% complete, ETA 00:00:53
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.java
# Parameters: (size = 6999)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 13.445 ns/op
# Warmup Iteration   2: 12.443 ns/op
# Warmup Iteration   3: 11.432 ns/op
# Warmup Iteration   4: 11.352 ns/op
# Warmup Iteration   5: 11.179 ns/op
Iteration   1: 11.457 ns/op
Iteration   2: 11.074 ns/op
Iteration   3: 11.412 ns/op
Iteration   4: 11.283 ns/op
Iteration   5: 11.034 ns/op

Result: 11.252 ±(99.9%) 0.740 ns/op [Average]
  Statistics: (min, avg, max) = (11.034, 11.252, 11.457), stdev = 0.192
  Confidence interval (99.9%): [10.512, 11.992]


# Run progress: 50.00% complete, ETA 00:00:40
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.java
# Parameters: (size = 16)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 13.155 ns/op
# Warmup Iteration   2: 12.439 ns/op
# Warmup Iteration   3: 12.031 ns/op
# Warmup Iteration   4: 11.201 ns/op
# Warmup Iteration   5: 11.831 ns/op
Iteration   1: 11.166 ns/op
Iteration   2: 11.245 ns/op
Iteration   3: 11.730 ns/op
Iteration   4: 11.053 ns/op
Iteration   5: 11.505 ns/op

Result: 11.340 ±(99.9%) 1.057 ns/op [Average]
  Statistics: (min, avg, max) = (11.053, 11.340, 11.730), stdev = 0.275
  Confidence interval (99.9%): [10.283, 12.397]


# Run progress: 66.67% complete, ETA 00:00:26
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.murmur
# Parameters: (size = 6999)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 36.238 ns/op
# Warmup Iteration   2: 32.511 ns/op
# Warmup Iteration   3: 29.699 ns/op
# Warmup Iteration   4: 28.455 ns/op
# Warmup Iteration   5: 28.555 ns/op
Iteration   1: 29.438 ns/op
Iteration   2: 28.037 ns/op
Iteration   3: 29.272 ns/op
Iteration   4: 27.790 ns/op
Iteration   5: 28.055 ns/op

Result: 28.518 ±(99.9%) 2.976 ns/op [Average]
  Statistics: (min, avg, max) = (27.790, 28.518, 29.438), stdev = 0.773
  Confidence interval (99.9%): [25.542, 31.495]


# Run progress: 83.33% complete, ETA 00:00:13
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.murmur
# Parameters: (size = 16)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 36.806 ns/op
# Warmup Iteration   2: 33.429 ns/op
# Warmup Iteration   3: 28.740 ns/op
# Warmup Iteration   4: 28.243 ns/op
# Warmup Iteration   5: 29.172 ns/op
Iteration   1: 27.839 ns/op
Iteration   2: 29.039 ns/op
Iteration   3: 28.628 ns/op
Iteration   4: 28.304 ns/op
Iteration   5: 28.885 ns/op

Result: 28.539 ±(99.9%) 1.849 ns/op [Average]
  Statistics: (min, avg, max) = (27.839, 28.539, 29.039), stdev = 0.480
  Confidence interval (99.9%): [26.690, 30.388]


# Run complete. Total time: 00:01:19

Benchmark                      (size)   Mode   Samples        Score  Score error    Units
c.g.w.c.HashBenchmark.atroll     6999   avgt         5       21.418        1.148    ns/op
c.g.w.c.HashBenchmark.atroll       16   avgt         5       21.281        1.205    ns/op
c.g.w.c.HashBenchmark.java       6999   avgt         5       11.252        0.740    ns/op
c.g.w.c.HashBenchmark.java         16   avgt         5       11.340        1.057    ns/op
c.g.w.c.HashBenchmark.murmur     6999   avgt         5       28.518        2.976    ns/op
c.g.w.c.HashBenchmark.murmur       16   avgt         5       28.539        1.849    ns/op

Run on a Debian box, release 7.6 (Wheezy) 64-bit
Linux Kernal 3.2.0-4-amd64
GNOME 3.4.2

with 2GB RAM and Intel® Pentium(R) Dual CPU E2200 @ 2.20GHz × 2
 */

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class HashBenchmark {
    private static final Object OBJECT = new Object();
    private static final int    hash   = 69;

    @Param({ "6999", "16" }) private int size;

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + HashBenchmark.class.getSimpleName() + ".*")
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }

    @Benchmark @Fork(1) public static void atroll(Blackhole hole) {
        hole.consume(AbstractHashStruct.HashStrategy.A_TROLL.hash(HashBenchmark.OBJECT, HashBenchmark.hash));
    }

    @Benchmark @Fork(1) public static void java(Blackhole hole) {
        hole.consume(AbstractHashStruct.HashStrategy.JAVA.hash(HashBenchmark.OBJECT, HashBenchmark.hash));
    }

    @Benchmark @Fork(1) public static void murmur(Blackhole hole) {
        hole.consume(AbstractHashStruct.HashStrategy.MURMUR.hash(HashBenchmark.OBJECT, HashBenchmark.hash));
    }
}

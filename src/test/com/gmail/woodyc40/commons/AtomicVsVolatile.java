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

import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/*
# Run progress: 0.00% complete, ETA 00:01:40
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readAto
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 1.061 ns/op
# Warmup Iteration   2: 1.037 ns/op
# Warmup Iteration   3: 1.010 ns/op
# Warmup Iteration   4: 1.004 ns/op
# Warmup Iteration   5: 1.023 ns/op
Iteration   1: 1.002 ns/op
Iteration   2: 1.001 ns/op
Iteration   3: 1.002 ns/op
Iteration   4: 1.001 ns/op
Iteration   5: 1.054 ns/op

Result: 1.012 ±(99.9%) 0.090 ns/op [Average]
  Statistics: (min, avg, max) = (1.001, 1.012, 1.054), stdev = 0.023
  Confidence interval (99.9%): [0.922, 1.102]


# Run progress: 10.00% complete, ETA 00:01:51
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readReg
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 0.896 ns/op
# Warmup Iteration   2: 0.893 ns/op
# Warmup Iteration   3: 0.887 ns/op
# Warmup Iteration   4: 0.888 ns/op
# Warmup Iteration   5: 0.890 ns/op
Iteration   1: 0.889 ns/op
Iteration   2: 0.890 ns/op
Iteration   3: 0.887 ns/op
Iteration   4: 0.888 ns/op
Iteration   5: 0.888 ns/op

Result: 0.888 ±(99.9%) 0.004 ns/op [Average]
  Statistics: (min, avg, max) = (0.887, 0.888, 0.890), stdev = 0.001
  Confidence interval (99.9%): [0.885, 0.892]


# Run progress: 20.00% complete, ETA 00:01:39
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readUnsafe
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 0.750 ns/op
# Warmup Iteration   2: 0.743 ns/op
# Warmup Iteration   3: 0.742 ns/op
# Warmup Iteration   4: 0.742 ns/op
# Warmup Iteration   5: 0.741 ns/op
Iteration   1: 0.739 ns/op
Iteration   2: 0.740 ns/op
Iteration   3: 0.740 ns/op
Iteration   4: 0.740 ns/op
Iteration   5: 0.741 ns/op

Result: 0.740 ±(99.9%) 0.003 ns/op [Average]
  Statistics: (min, avg, max) = (0.739, 0.740, 0.741), stdev = 0.001
  Confidence interval (99.9%): [0.737, 0.743]


# Run progress: 30.00% complete, ETA 00:01:26
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readVol
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 0.899 ns/op
# Warmup Iteration   2: 0.896 ns/op
# Warmup Iteration   3: 0.890 ns/op
# Warmup Iteration   4: 0.888 ns/op
# Warmup Iteration   5: 0.888 ns/op
Iteration   1: 0.888 ns/op
Iteration   2: 0.890 ns/op
Iteration   3: 0.890 ns/op
Iteration   4: 0.891 ns/op
Iteration   5: 0.887 ns/op

Result: 0.889 ±(99.9%) 0.006 ns/op [Average]
  Statistics: (min, avg, max) = (0.887, 0.889, 0.891), stdev = 0.001
  Confidence interval (99.9%): [0.884, 0.895]


# Run progress: 40.00% complete, ETA 00:01:14
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeAto
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.553 ns/op
# Warmup Iteration   2: 6.498 ns/op
# Warmup Iteration   3: 6.495 ns/op
# Warmup Iteration   4: 6.497 ns/op
# Warmup Iteration   5: 6.501 ns/op
Iteration   1: 6.504 ns/op
Iteration   2: 6.500 ns/op
Iteration   3: 6.498 ns/op
Iteration   4: 6.493 ns/op
Iteration   5: 6.498 ns/op

Result: 6.499 ±(99.9%) 0.014 ns/op [Average]
  Statistics: (min, avg, max) = (6.493, 6.499, 6.504), stdev = 0.004
  Confidence interval (99.9%): [6.484, 6.513]


# Run progress: 50.00% complete, ETA 00:01:02
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeAto0
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.540 ns/op
# Warmup Iteration   2: 6.517 ns/op
# Warmup Iteration   3: 6.501 ns/op
# Warmup Iteration   4: 6.498 ns/op
# Warmup Iteration   5: 6.497 ns/op
Iteration   1: 6.496 ns/op
Iteration   2: 6.496 ns/op
Iteration   3: 6.497 ns/op
Iteration   4: 6.498 ns/op
Iteration   5: 6.501 ns/op

Result: 6.498 ±(99.9%) 0.008 ns/op [Average]
  Statistics: (min, avg, max) = (6.496, 6.498, 6.501), stdev = 0.002
  Confidence interval (99.9%): [6.489, 6.506]


# Run progress: 60.00% complete, ETA 00:00:49
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeReg
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 0.593 ns/op
# Warmup Iteration   2: 0.597 ns/op
# Warmup Iteration   3: 0.591 ns/op
# Warmup Iteration   4: 0.591 ns/op
# Warmup Iteration   5: 0.598 ns/op
Iteration   1: 0.592 ns/op
Iteration   2: 0.591 ns/op
Iteration   3: 0.591 ns/op
Iteration   4: 0.591 ns/op
Iteration   5: 0.599 ns/op

Result: 0.593 ±(99.9%) 0.014 ns/op [Average]
  Statistics: (min, avg, max) = (0.591, 0.593, 0.599), stdev = 0.004
  Confidence interval (99.9%): [0.579, 0.607]


# Run progress: 70.00% complete, ETA 00:00:37
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeUnsafe
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.534 ns/op
# Warmup Iteration   2: 6.496 ns/op
# Warmup Iteration   3: 6.494 ns/op
# Warmup Iteration   4: 6.495 ns/op
# Warmup Iteration   5: 6.496 ns/op
Iteration   1: 6.496 ns/op
Iteration   2: 6.497 ns/op
Iteration   3: 6.495 ns/op
Iteration   4: 6.493 ns/op
Iteration   5: 6.497 ns/op

Result: 6.496 ±(99.9%) 0.007 ns/op [Average]
  Statistics: (min, avg, max) = (6.493, 6.496, 6.497), stdev = 0.002
  Confidence interval (99.9%): [6.488, 6.503]


# Run progress: 80.00% complete, ETA 00:00:24
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeUnsafe0
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.541 ns/op
# Warmup Iteration   2: 6.501 ns/op
# Warmup Iteration   3: 6.497 ns/op
# Warmup Iteration   4: 6.500 ns/op
# Warmup Iteration   5: 6.497 ns/op
Iteration   1: 6.495 ns/op
Iteration   2: 6.496 ns/op
Iteration   3: 6.497 ns/op
Iteration   4: 6.498 ns/op
Iteration   5: 6.495 ns/op

Result: 6.496 ±(99.9%) 0.005 ns/op [Average]
  Statistics: (min, avg, max) = (6.495, 6.496, 6.498), stdev = 0.001
  Confidence interval (99.9%): [6.491, 6.501]


# Run progress: 90.00% complete, ETA 00:00:12
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeVol
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.518 ns/op
# Warmup Iteration   2: 6.509 ns/op
# Warmup Iteration   3: 6.539 ns/op
# Warmup Iteration   4: 6.498 ns/op
# Warmup Iteration   5: 6.494 ns/op
Iteration   1: 6.494 ns/op
Iteration   2: 6.493 ns/op
Iteration   3: 6.493 ns/op
Iteration   4: 6.493 ns/op
Iteration   5: 6.494 ns/op

Result: 6.493 ±(99.9%) 0.002 ns/op [Average]
  Statistics: (min, avg, max) = (6.493, 6.493, 6.494), stdev = 0.001
  Confidence interval (99.9%): [6.491, 6.495]


# Run complete. Total time: 00:02:04

Benchmark                                 Mode   Samples        Score  Score error    Units
c.g.w.c.AtomicVsVolatile.readAto          avgt         5        1.012        0.090    ns/op
c.g.w.c.AtomicVsVolatile.readReg          avgt         5        0.888        0.004    ns/op
c.g.w.c.AtomicVsVolatile.readUnsafe       avgt         5        0.740        0.003    ns/op
c.g.w.c.AtomicVsVolatile.readVol          avgt         5        0.889        0.006    ns/op
c.g.w.c.AtomicVsVolatile.writeAto         avgt         5        6.499        0.014    ns/op
c.g.w.c.AtomicVsVolatile.writeAto0        avgt         5        6.498        0.008    ns/op
c.g.w.c.AtomicVsVolatile.writeReg         avgt         5        0.593        0.014    ns/op
c.g.w.c.AtomicVsVolatile.writeUnsafe      avgt         5        6.496        0.007    ns/op
c.g.w.c.AtomicVsVolatile.writeUnsafe0     avgt         5        6.496        0.005    ns/op
c.g.w.c.AtomicVsVolatile.writeVol         avgt         5        6.493        0.002    ns/op
 */
public class AtomicVsVolatile {
    private static final    Unsafe        UNSAFE   = UnsafeProvider.getProvider();
    private static final    long          offset   =
            AtomicVsVolatile.UNSAFE.staticFieldOffset(ReflectionTool.forField("aBoolean", AtomicVsVolatile.class));
    private static volatile boolean       vol      = true;
    private static          AtomicBoolean ato      = new AtomicBoolean(true);
    private static          boolean       aBoolean = true;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + AtomicVsVolatile.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark public void readVol(Blackhole blackhole) {
        blackhole.consume(AtomicVsVolatile.vol);
    }

    @Benchmark public void writeVol() {
        AtomicVsVolatile.vol = true;
    }

    @Benchmark public void readAto(Blackhole blackhole) {
        blackhole.consume(AtomicVsVolatile.ato.get());
    }

    @Benchmark public void writeAto() {
        AtomicVsVolatile.ato.compareAndSet(true, false);
    }

    @Benchmark public void writeAto0() {
        AtomicVsVolatile.ato.set(true);
    }

    @Benchmark public void readUnsafe(Blackhole blackhole) {
        blackhole.consume(AtomicVsVolatile.UNSAFE.getBooleanVolatile(AtomicVsVolatile.class, AtomicVsVolatile.offset));
    }

    @Benchmark public void readReg(Blackhole blackhole) {
        blackhole.consume(AtomicVsVolatile.aBoolean);
    }

    @Benchmark public void writeReg() {
        AtomicVsVolatile.aBoolean = false;
    }

    @Benchmark public void writeUnsafe() {
        AtomicVsVolatile.UNSAFE.putBooleanVolatile(AtomicVsVolatile.class, AtomicVsVolatile.offset, false);
    }

    @Benchmark public void writeUnsafe0() {
        AtomicVsVolatile.UNSAFE.compareAndSwapObject(AtomicVsVolatile.class, AtomicVsVolatile.offset, false, true);
    }
}

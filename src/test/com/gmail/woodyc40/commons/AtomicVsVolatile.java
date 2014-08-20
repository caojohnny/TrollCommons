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
# Run progress: 0.00% complete, ETA 00:01:10
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readAto
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 1.202 ns/op
# Warmup Iteration   2: 1.192 ns/op
# Warmup Iteration   3: 1.181 ns/op
# Warmup Iteration   4: 1.182 ns/op
# Warmup Iteration   5: 1.183 ns/op
Iteration   1: 1.183 ns/op
Iteration   2: 1.182 ns/op
Iteration   3: 1.182 ns/op
Iteration   4: 1.197 ns/op
Iteration   5: 1.182 ns/op

Result: 1.185 ±(99.9%) 0.025 ns/op [Average]
  Statistics: (min, avg, max) = (1.182, 1.185, 1.197), stdev = 0.006
  Confidence interval (99.9%): [1.160, 1.210]


# Run progress: 14.29% complete, ETA 00:01:13
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readUnsafe
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 0.750 ns/op
# Warmup Iteration   2: 0.744 ns/op
# Warmup Iteration   3: 0.741 ns/op
# Warmup Iteration   4: 0.738 ns/op
# Warmup Iteration   5: 0.738 ns/op
Iteration   1: 0.739 ns/op
Iteration   2: 0.740 ns/op
Iteration   3: 0.738 ns/op
Iteration   4: 0.738 ns/op
Iteration   5: 0.740 ns/op

Result: 0.739 ±(99.9%) 0.004 ns/op [Average]
  Statistics: (min, avg, max) = (0.738, 0.739, 0.740), stdev = 0.001
  Confidence interval (99.9%): [0.736, 0.743]


# Run progress: 28.57% complete, ETA 00:01:01
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readVol
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 0.896 ns/op
# Warmup Iteration   2: 0.889 ns/op
# Warmup Iteration   3: 0.889 ns/op
# Warmup Iteration   4: 0.886 ns/op
# Warmup Iteration   5: 0.886 ns/op
Iteration   1: 0.887 ns/op
Iteration   2: 0.886 ns/op
Iteration   3: 0.887 ns/op
Iteration   4: 0.887 ns/op
Iteration   5: 0.887 ns/op

Result: 0.887 ±(99.9%) 0.002 ns/op [Average]
  Statistics: (min, avg, max) = (0.886, 0.887, 0.887), stdev = 0.001
  Confidence interval (99.9%): [0.884, 0.889]


# Run progress: 42.86% complete, ETA 00:00:49
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeAto
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.526 ns/op
# Warmup Iteration   2: 6.496 ns/op
# Warmup Iteration   3: 6.492 ns/op
# Warmup Iteration   4: 6.492 ns/op
# Warmup Iteration   5: 6.493 ns/op
Iteration   1: 6.492 ns/op
Iteration   2: 6.494 ns/op
Iteration   3: 6.492 ns/op
Iteration   4: 6.492 ns/op
Iteration   5: 6.490 ns/op

Result: 6.492 ±(99.9%) 0.005 ns/op [Average]
  Statistics: (min, avg, max) = (6.490, 6.492, 6.494), stdev = 0.001
  Confidence interval (99.9%): [6.486, 6.497]


# Run progress: 57.14% complete, ETA 00:00:37
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeAto0
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.536 ns/op
# Warmup Iteration   2: 6.495 ns/op
# Warmup Iteration   3: 6.491 ns/op
# Warmup Iteration   4: 6.529 ns/op
# Warmup Iteration   5: 6.492 ns/op
Iteration   1: 6.496 ns/op
Iteration   2: 6.492 ns/op
Iteration   3: 6.493 ns/op
Iteration   4: 6.492 ns/op
Iteration   5: 6.493 ns/op

Result: 6.493 ±(99.9%) 0.006 ns/op [Average]
  Statistics: (min, avg, max) = (6.492, 6.493, 6.496), stdev = 0.001
  Confidence interval (99.9%): [6.488, 6.499]


# Run progress: 71.43% complete, ETA 00:00:24
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeUnsafe
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.528 ns/op
# Warmup Iteration   2: 6.497 ns/op
# Warmup Iteration   3: 6.493 ns/op
# Warmup Iteration   4: 6.494 ns/op
# Warmup Iteration   5: 6.507 ns/op
Iteration   1: 6.492 ns/op
Iteration   2: 6.493 ns/op
Iteration   3: 6.493 ns/op
Iteration   4: 6.491 ns/op
Iteration   5: 6.492 ns/op

Result: 6.492 ±(99.9%) 0.003 ns/op [Average]
  Statistics: (min, avg, max) = (6.491, 6.492, 6.493), stdev = 0.001
  Confidence interval (99.9%): [6.489, 6.495]


# Run progress: 85.71% complete, ETA 00:00:12
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeVol
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.517 ns/op
# Warmup Iteration   2: 6.499 ns/op
# Warmup Iteration   3: 6.489 ns/op
# Warmup Iteration   4: 6.512 ns/op
# Warmup Iteration   5: 6.491 ns/op
Iteration   1: 6.490 ns/op
Iteration   2: 6.492 ns/op
Iteration   3: 6.500 ns/op
Iteration   4: 6.492 ns/op
Iteration   5: 6.491 ns/op

Result: 6.493 ±(99.9%) 0.015 ns/op [Average]
  Statistics: (min, avg, max) = (6.490, 6.493, 6.500), stdev = 0.004
  Confidence interval (99.9%): [6.478, 6.508]


# Run complete. Total time: 00:01:26

Benchmark                                Mode   Samples        Score  Score error    Units
c.g.w.c.AtomicVsVolatile.readAto         avgt         5        1.185        0.025    ns/op
c.g.w.c.AtomicVsVolatile.readUnsafe      avgt         5        0.739        0.004    ns/op
c.g.w.c.AtomicVsVolatile.readVol         avgt         5        0.887        0.002    ns/op
c.g.w.c.AtomicVsVolatile.writeAto        avgt         5        6.492        0.005    ns/op
c.g.w.c.AtomicVsVolatile.writeAto0       avgt         5        6.493        0.006    ns/op
c.g.w.c.AtomicVsVolatile.writeUnsafe     avgt         5        6.492        0.003    ns/op
c.g.w.c.AtomicVsVolatile.writeVol        avgt         5        6.493        0.015    ns/op
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

    @Benchmark public void writeUnsafe() {
        AtomicVsVolatile.UNSAFE.putBooleanVolatile(AtomicVsVolatile.class, AtomicVsVolatile.offset, false);
    }
}

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
Update: Results were found to be seriously flawed since they were run in a single thread

# Run progress: 0.00% complete, ETA 00:01:40
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readAto
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6.178 ns/op
# Warmup Iteration   2: 6.110 ns/op
# Warmup Iteration   3: 5.863 ns/op
# Warmup Iteration   4: 5.823 ns/op
# Warmup Iteration   5: 5.953 ns/op
Iteration   1: 6.070 ns/op
Iteration   2: 5.936 ns/op
Iteration   3: 6.281 ns/op
Iteration   4: 5.937 ns/op
Iteration   5: 5.998 ns/op

Result: 6.044 ±(99.9%) 0.552 ns/op [Average]
  Statistics: (min, avg, max) = (5.936, 6.044, 6.281), stdev = 0.143
  Confidence interval (99.9%): [5.492, 6.597]


# Run progress: 10.00% complete, ETA 00:01:52
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readReg
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 4.416 ns/op
# Warmup Iteration   2: 4.453 ns/op
# Warmup Iteration   3: 4.289 ns/op
# Warmup Iteration   4: 4.332 ns/op
# Warmup Iteration   5: 4.349 ns/op
Iteration   1: 4.522 ns/op
Iteration   2: 4.685 ns/op
Iteration   3: 4.566 ns/op
Iteration   4: 4.600 ns/op
Iteration   5: 4.615 ns/op

Result: 4.598 ±(99.9%) 0.234 ns/op [Average]
  Statistics: (min, avg, max) = (4.522, 4.598, 4.685), stdev = 0.061
  Confidence interval (99.9%): [4.364, 4.831]


# Run progress: 20.00% complete, ETA 00:01:39
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readUnsafe
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 4.843 ns/op
# Warmup Iteration   2: 4.774 ns/op
# Warmup Iteration   3: 4.796 ns/op
# Warmup Iteration   4: 4.982 ns/op
# Warmup Iteration   5: 4.948 ns/op
Iteration   1: 4.887 ns/op
Iteration   2: 4.462 ns/op
Iteration   3: 4.571 ns/op
Iteration   4: 4.912 ns/op
Iteration   5: 4.911 ns/op

Result: 4.749 ±(99.9%) 0.830 ns/op [Average]
  Statistics: (min, avg, max) = (4.462, 4.749, 4.912), stdev = 0.216
  Confidence interval (99.9%): [3.918, 5.579]


# Run progress: 30.00% complete, ETA 00:01:27
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.readVol
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 4.754 ns/op
# Warmup Iteration   2: 4.800 ns/op
# Warmup Iteration   3: 4.453 ns/op
# Warmup Iteration   4: 4.630 ns/op
# Warmup Iteration   5: 4.551 ns/op
Iteration   1: 4.609 ns/op
Iteration   2: 4.854 ns/op
Iteration   3: 4.494 ns/op
Iteration   4: 4.630 ns/op
Iteration   5: 4.545 ns/op

Result: 4.626 ±(99.9%) 0.532 ns/op [Average]
  Statistics: (min, avg, max) = (4.494, 4.626, 4.854), stdev = 0.138
  Confidence interval (99.9%): [4.095, 5.158]


# Run progress: 40.00% complete, ETA 00:01:14
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeAto
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 241.697 ns/op
# Warmup Iteration   2: 241.579 ns/op
# Warmup Iteration   3: 227.626 ns/op
# Warmup Iteration   4: 179.265 ns/op
# Warmup Iteration   5: 176.694 ns/op
Iteration   1: 185.507 ns/op
Iteration   2: 171.769 ns/op
Iteration   3: 184.793 ns/op
Iteration   4: 247.729 ns/op
Iteration   5: 243.975 ns/op

Result: 206.754 ±(99.9%) 139.131 ns/op [Average]
  Statistics: (min, avg, max) = (171.769, 206.754, 247.729), stdev = 36.132
  Confidence interval (99.9%): [67.623, 345.886]


# Run progress: 50.00% complete, ETA 00:01:02
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeAto0
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 96.600 ns/op
# Warmup Iteration   2: 98.401 ns/op
# Warmup Iteration   3: 96.825 ns/op
# Warmup Iteration   4: 100.635 ns/op
# Warmup Iteration   5: 102.219 ns/op
Iteration   1: 95.097 ns/op
Iteration   2: 94.957 ns/op
Iteration   3: 95.784 ns/op
Iteration   4: 96.231 ns/op
Iteration   5: 96.192 ns/op

Result: 95.652 ±(99.9%) 2.307 ns/op [Average]
  Statistics: (min, avg, max) = (94.957, 95.652, 96.231), stdev = 0.599
  Confidence interval (99.9%): [93.345, 97.960]


# Run progress: 60.00% complete, ETA 00:00:49
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeReg
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 3.424 ns/op
# Warmup Iteration   2: 3.826 ns/op
# Warmup Iteration   3: 3.643 ns/op
# Warmup Iteration   4: 4.024 ns/op
# Warmup Iteration   5: 3.899 ns/op
Iteration   1: 3.588 ns/op
Iteration   2: 3.676 ns/op
Iteration   3: 3.576 ns/op
Iteration   4: 3.544 ns/op
Iteration   5: 3.486 ns/op

Result: 3.574 ±(99.9%) 0.267 ns/op [Average]
  Statistics: (min, avg, max) = (3.486, 3.574, 3.676), stdev = 0.069
  Confidence interval (99.9%): [3.307, 3.841]


# Run progress: 70.00% complete, ETA 00:00:37
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeUnsafe
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 77.597 ns/op
# Warmup Iteration   2: 75.829 ns/op
# Warmup Iteration   3: 76.191 ns/op
# Warmup Iteration   4: 77.347 ns/op
# Warmup Iteration   5: 75.808 ns/op
Iteration   1: 76.656 ns/op
Iteration   2: 75.843 ns/op
Iteration   3: 74.094 ns/op
Iteration   4: 77.432 ns/op
Iteration   5: 76.514 ns/op

Result: 76.108 ±(99.9%) 4.850 ns/op [Average]
  Statistics: (min, avg, max) = (74.094, 76.108, 77.432), stdev = 1.260
  Confidence interval (99.9%): [71.258, 80.958]


# Run progress: 80.00% complete, ETA 00:00:24
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeUnsafe0
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 8195.067 ns/op
# Warmup Iteration   2: 7991.846 ns/op
# Warmup Iteration   3: 8029.835 ns/op
# Warmup Iteration   4: 8041.251 ns/op
# Warmup Iteration   5: 8268.820 ns/op
Iteration   1: 8003.886 ns/op
Iteration   2: 8185.579 ns/op
Iteration   3: 8066.163 ns/op
Iteration   4: 8089.824 ns/op
Iteration   5: 8201.883 ns/op

Result: 8109.467 ±(99.9%) 320.681 ns/op [Average]
  Statistics: (min, avg, max) = (8003.886, 8109.467, 8201.883), stdev = 83.280
  Confidence interval (99.9%): [7788.786, 8430.148]


# Run progress: 90.00% complete, ETA 00:00:12
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 10 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.AtomicVsVolatile.writeVol
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 82.244 ns/op
# Warmup Iteration   2: 83.304 ns/op
# Warmup Iteration   3: 83.315 ns/op
# Warmup Iteration   4: 82.222 ns/op
# Warmup Iteration   5: 83.199 ns/op
Iteration   1: 85.496 ns/op
Iteration   2: 85.131 ns/op
Iteration   3: 85.939 ns/op
Iteration   4: 83.040 ns/op
Iteration   5: 87.424 ns/op

Result: 85.406 ±(99.9%) 6.099 ns/op [Average]
  Statistics: (min, avg, max) = (83.040, 85.406, 87.424), stdev = 1.584
  Confidence interval (99.9%): [79.306, 91.505]


# Run complete. Total time: 00:02:04

Benchmark                                 Mode   Samples        Score  Score error    Units
c.g.w.c.AtomicVsVolatile.readAto          avgt         5        6.044        0.552    ns/op
c.g.w.c.AtomicVsVolatile.readReg          avgt         5        4.598        0.234    ns/op
c.g.w.c.AtomicVsVolatile.readUnsafe       avgt         5        4.749        0.830    ns/op
c.g.w.c.AtomicVsVolatile.readVol          avgt         5        4.626        0.532    ns/op
c.g.w.c.AtomicVsVolatile.writeAto         avgt         5      206.754      139.131    ns/op
c.g.w.c.AtomicVsVolatile.writeAto0        avgt         5       95.652        2.307    ns/op
c.g.w.c.AtomicVsVolatile.writeReg         avgt         5        3.574        0.267    ns/op
c.g.w.c.AtomicVsVolatile.writeUnsafe      avgt         5       76.108        4.850    ns/op
c.g.w.c.AtomicVsVolatile.writeUnsafe0     avgt         5     8109.467      320.681    ns/op
c.g.w.c.AtomicVsVolatile.writeVol         avgt         5       85.406        6.099    ns/op
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
                .threads(10)
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

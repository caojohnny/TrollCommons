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
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/*
# Run progress: 0.00% complete, ETA 00:00:20
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.UnsafeProviderTest.getUnsafe
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7533 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 29.277 ns/op
# Warmup Iteration   2: 29.780 ns/op
# Warmup Iteration   3: 30.756 ns/op
# Warmup Iteration   4: 30.814 ns/op
# Warmup Iteration   5: 30.497 ns/op
Iteration   1: 29.440 ns/op
Iteration   2: 29.958 ns/op
Iteration   3: 29.982 ns/op
Iteration   4: 29.790 ns/op
Iteration   5: 29.812 ns/op

Result: 29.796 ±(99.9%) 0.834 ns/op [Average]
  Statistics: (min, avg, max) = (29.440, 29.796, 29.982), stdev = 0.217
  Confidence interval (99.9%): [28.962, 30.631]


# Run progress: 50.00% complete, ETA 00:00:12
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.UnsafeProviderTest.getUnsafe0
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7533 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 9.172 ns/op
# Warmup Iteration   2: 8.944 ns/op
# Warmup Iteration   3: 8.882 ns/op
# Warmup Iteration   4: 8.946 ns/op
# Warmup Iteration   5: 8.809 ns/op
Iteration   1: 8.900 ns/op
Iteration   2: 8.953 ns/op
Iteration   3: 8.914 ns/op
Iteration   4: 8.788 ns/op
Iteration   5: 8.843 ns/op

Result: 8.879 ±(99.9%) 0.248 ns/op [Average]
  Statistics: (min, avg, max) = (8.788, 8.879, 8.953), stdev = 0.065
  Confidence interval (99.9%): [8.631, 9.128]


# Run complete. Total time: 00:00:24

Benchmark                                 Mode   Samples        Score  Score error    Units
c.g.w.c.UnsafeProviderTest.getUnsafe      avgt         5       29.796        0.834    ns/op
c.g.w.c.UnsafeProviderTest.getUnsafe0     avgt         5        8.879        0.248    ns/op
 */
@State(Scope.Benchmark)
public class UnsafeProviderTest {
    private static Field FIELD;
    static {
        try {
            UnsafeProviderTest.FIELD = UnsafeProviderTest.class.getDeclaredField("FIELD");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void main0(String... args) {
        // Reflection - Prints
        // sun.misc.Unsafe
        System.out.println(UnsafeProvider.acquireField(ReflectionTool.forField("PROVIDER", UnsafeProvider.class), null)
                .getClass().getName());

        // Unsafe cast - Prints
        // 2337
        // 8451275
        // java.lang.Object@4741d6
        // 4669910
        String string = "Hi";
        Object dummy = new Object();

        System.out.println(string.hashCode());
        System.out.println(dummy.hashCode());

        Object ret = UnsafeProvider.castSuper("Hi", dummy);
        System.out.println(ret);
        System.out.println(ret.hashCode());
    }

    @Benchmark public static void getUnsafe(Blackhole blackhole) {
        blackhole.consume(UnsafeProvider.fieldOffset(UnsafeProviderTest.FIELD));
    }

    @Benchmark public static void getUnsafe0(Blackhole blackhole) {
        blackhole.consume(UnsafeProvider.fieldOffset0(UnsafeProviderTest.FIELD));
    }

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + UnsafeProviderTest.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

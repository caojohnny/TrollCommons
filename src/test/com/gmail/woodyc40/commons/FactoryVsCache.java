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
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
Null method accessor:
# Run progress: 0.00% complete, ETA 00:01:40
# Warmup: 25 iterations, 1 s each
# Measurement: 25 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.FactoryVsCache.cache
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 7.246 ns/op
# Warmup Iteration   2: 7.093 ns/op
# Warmup Iteration   3: 6.813 ns/op
# Warmup Iteration   4: 6.823 ns/op
# Warmup Iteration   5: 7.030 ns/op
# Warmup Iteration   6: 6.982 ns/op
# Warmup Iteration   7: 6.990 ns/op
# Warmup Iteration   8: 6.808 ns/op
# Warmup Iteration   9: 6.976 ns/op
# Warmup Iteration  10: 6.890 ns/op
# Warmup Iteration  11: 6.822 ns/op
# Warmup Iteration  12: 6.775 ns/op
# Warmup Iteration  13: 6.817 ns/op
# Warmup Iteration  14: 6.783 ns/op
# Warmup Iteration  15: 6.940 ns/op
# Warmup Iteration  16: 6.836 ns/op
# Warmup Iteration  17: 6.775 ns/op
# Warmup Iteration  18: 6.792 ns/op
# Warmup Iteration  19: 6.770 ns/op
# Warmup Iteration  20: 6.772 ns/op
# Warmup Iteration  21: 6.778 ns/op
# Warmup Iteration  22: 6.883 ns/op
# Warmup Iteration  23: 6.780 ns/op
# Warmup Iteration  24: 6.785 ns/op
# Warmup Iteration  25: 6.781 ns/op
Iteration   1: 6.835 ns/op
Iteration   2: 6.841 ns/op
Iteration   3: 6.783 ns/op
Iteration   4: 6.776 ns/op
Iteration   5: 6.820 ns/op
Iteration   6: 6.776 ns/op
Iteration   7: 6.783 ns/op
Iteration   8: 6.784 ns/op
Iteration   9: 6.789 ns/op
Iteration  10: 6.810 ns/op
Iteration  11: 6.777 ns/op
Iteration  12: 6.802 ns/op
Iteration  13: 6.774 ns/op
Iteration  14: 6.826 ns/op
Iteration  15: 6.791 ns/op
Iteration  16: 6.809 ns/op
Iteration  17: 6.854 ns/op
Iteration  18: 7.004 ns/op
Iteration  19: 6.981 ns/op
Iteration  20: 6.975 ns/op
Iteration  21: 7.004 ns/op
Iteration  22: 6.806 ns/op
Iteration  23: 6.844 ns/op
Iteration  24: 6.851 ns/op
Iteration  25: 6.882 ns/op

Result: 6.839 ±(99.9%) 0.055 ns/op [Average]
  Statistics: (min, avg, max) = (6.774, 6.839, 7.004), stdev = 0.074
  Confidence interval (99.9%): [6.784, 6.894]


# Run progress: 50.00% complete, ETA 00:01:00
# Warmup: 25 iterations, 1 s each
# Measurement: 25 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.FactoryVsCache.factory
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 10.648 ns/op
# Warmup Iteration   2: 9.328 ns/op
# Warmup Iteration   3: 8.926 ns/op
# Warmup Iteration   4: 8.897 ns/op
# Warmup Iteration   5: 8.850 ns/op
# Warmup Iteration   6: 8.875 ns/op
# Warmup Iteration   7: 8.962 ns/op
# Warmup Iteration   8: 8.943 ns/op
# Warmup Iteration   9: 9.079 ns/op
# Warmup Iteration  10: 8.973 ns/op
# Warmup Iteration  11: 8.941 ns/op
# Warmup Iteration  12: 9.065 ns/op
# Warmup Iteration  13: 9.006 ns/op
# Warmup Iteration  14: 8.954 ns/op
# Warmup Iteration  15: 9.012 ns/op
# Warmup Iteration  16: 8.885 ns/op
# Warmup Iteration  17: 9.057 ns/op
# Warmup Iteration  18: 9.019 ns/op
# Warmup Iteration  19: 8.963 ns/op
# Warmup Iteration  20: 8.921 ns/op
# Warmup Iteration  21: 8.982 ns/op
# Warmup Iteration  22: 8.956 ns/op
# Warmup Iteration  23: 8.990 ns/op
# Warmup Iteration  24: 9.104 ns/op
# Warmup Iteration  25: 9.106 ns/op
Iteration   1: 9.003 ns/op
Iteration   2: 9.084 ns/op
Iteration   3: 8.916 ns/op
Iteration   4: 9.072 ns/op
Iteration   5: 9.011 ns/op
Iteration   6: 8.915 ns/op
Iteration   7: 8.942 ns/op
Iteration   8: 9.009 ns/op
Iteration   9: 8.996 ns/op
Iteration  10: 8.905 ns/op
Iteration  11: 9.100 ns/op
Iteration  12: 9.039 ns/op
Iteration  13: 8.966 ns/op
Iteration  14: 8.998 ns/op
Iteration  15: 8.946 ns/op
Iteration  16: 9.083 ns/op
Iteration  17: 9.034 ns/op
Iteration  18: 9.060 ns/op
Iteration  19: 8.954 ns/op
Iteration  20: 8.918 ns/op
Iteration  21: 8.957 ns/op
Iteration  22: 9.017 ns/op
Iteration  23: 8.992 ns/op
Iteration  24: 9.003 ns/op
Iteration  25: 8.981 ns/op

Result: 8.996 ±(99.9%) 0.043 ns/op [Average]
  Statistics: (min, avg, max) = (8.905, 8.996, 9.100), stdev = 0.057
  Confidence interval (99.9%): [8.953, 9.039]


# Run complete. Total time: 00:02:00

Benchmark                          Mode   Samples        Score  Score error    Units
c.g.w.c.FactoryVsCache.cache       avgt        25        6.839        0.055    ns/op
c.g.w.c.FactoryVsCache.factory     avgt        25        8.996        0.043    ns/op

========================================================================================================================
Valid method accessor:
# Run progress: 0.00% complete, ETA 00:01:40
# Warmup: 25 iterations, 1 s each
# Measurement: 25 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.FactoryVsCache.cache
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7533 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 7.373 ns/op
# Warmup Iteration   2: 7.299 ns/op
# Warmup Iteration   3: 6.814 ns/op
# Warmup Iteration   4: 6.796 ns/op
# Warmup Iteration   5: 7.007 ns/op
# Warmup Iteration   6: 6.997 ns/op
# Warmup Iteration   7: 7.001 ns/op
# Warmup Iteration   8: 6.996 ns/op
# Warmup Iteration   9: 6.789 ns/op
# Warmup Iteration  10: 6.800 ns/op
# Warmup Iteration  11: 6.892 ns/op
# Warmup Iteration  12: 6.971 ns/op
# Warmup Iteration  13: 6.791 ns/op
# Warmup Iteration  14: 6.816 ns/op
# Warmup Iteration  15: 6.785 ns/op
# Warmup Iteration  16: 6.950 ns/op
# Warmup Iteration  17: 6.856 ns/op
# Warmup Iteration  18: 6.796 ns/op
# Warmup Iteration  19: 6.760 ns/op
# Warmup Iteration  20: 6.793 ns/op
# Warmup Iteration  21: 6.814 ns/op
# Warmup Iteration  22: 6.832 ns/op
# Warmup Iteration  23: 6.922 ns/op
# Warmup Iteration  24: 6.973 ns/op
# Warmup Iteration  25: 6.955 ns/op
Iteration   1: 6.768 ns/op
Iteration   2: 6.773 ns/op
Iteration   3: 6.813 ns/op
Iteration   4: 6.806 ns/op
Iteration   5: 6.948 ns/op
Iteration   6: 6.990 ns/op
Iteration   7: 7.012 ns/op
Iteration   8: 7.012 ns/op
Iteration   9: 6.982 ns/op
Iteration  10: 6.764 ns/op
Iteration  11: 6.800 ns/op
Iteration  12: 6.777 ns/op
Iteration  13: 6.974 ns/op
Iteration  14: 6.766 ns/op
Iteration  15: 6.936 ns/op
Iteration  16: 6.750 ns/op
Iteration  17: 6.968 ns/op
Iteration  18: 6.754 ns/op
Iteration  19: 6.898 ns/op
Iteration  20: 6.952 ns/op
Iteration  21: 6.785 ns/op
Iteration  22: 6.938 ns/op
Iteration  23: 6.771 ns/op
Iteration  24: 6.897 ns/op
Iteration  25: 6.815 ns/op

Result: 6.866 ±(99.9%) 0.072 ns/op [Average]
  Statistics: (min, avg, max) = (6.750, 6.866, 7.012), stdev = 0.096
  Confidence interval (99.9%): [6.794, 6.938]


# Run progress: 50.00% complete, ETA 00:01:00
# Warmup: 25 iterations, 1 s each
# Measurement: 25 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.FactoryVsCache.factory
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7533 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 10.670 ns/op
# Warmup Iteration   2: 9.389 ns/op
# Warmup Iteration   3: 8.757 ns/op
# Warmup Iteration   4: 8.845 ns/op
# Warmup Iteration   5: 8.859 ns/op
# Warmup Iteration   6: 8.950 ns/op
# Warmup Iteration   7: 8.932 ns/op
# Warmup Iteration   8: 9.049 ns/op
# Warmup Iteration   9: 8.945 ns/op
# Warmup Iteration  10: 8.900 ns/op
# Warmup Iteration  11: 9.004 ns/op
# Warmup Iteration  12: 9.051 ns/op
# Warmup Iteration  13: 8.909 ns/op
# Warmup Iteration  14: 9.017 ns/op
# Warmup Iteration  15: 9.003 ns/op
# Warmup Iteration  16: 8.928 ns/op
# Warmup Iteration  17: 8.921 ns/op
# Warmup Iteration  18: 9.079 ns/op
# Warmup Iteration  19: 8.990 ns/op
# Warmup Iteration  20: 8.967 ns/op
# Warmup Iteration  21: 8.985 ns/op
# Warmup Iteration  22: 9.080 ns/op
# Warmup Iteration  23: 8.959 ns/op
# Warmup Iteration  24: 8.925 ns/op
# Warmup Iteration  25: 8.931 ns/op
Iteration   1: 8.978 ns/op
Iteration   2: 8.978 ns/op
Iteration   3: 9.029 ns/op
Iteration   4: 8.992 ns/op
Iteration   5: 8.879 ns/op
Iteration   6: 8.881 ns/op
Iteration   7: 8.942 ns/op
Iteration   8: 8.915 ns/op
Iteration   9: 8.890 ns/op
Iteration  10: 8.957 ns/op
Iteration  11: 9.024 ns/op
Iteration  12: 8.888 ns/op
Iteration  13: 8.935 ns/op
Iteration  14: 8.959 ns/op
Iteration  15: 9.331 ns/op
Iteration  16: 8.987 ns/op
Iteration  17: 8.893 ns/op
Iteration  18: 8.960 ns/op
Iteration  19: 8.964 ns/op
Iteration  20: 8.935 ns/op
Iteration  21: 9.008 ns/op
Iteration  22: 9.073 ns/op
Iteration  23: 8.993 ns/op
Iteration  24: 9.003 ns/op
Iteration  25: 8.955 ns/op

Result: 8.974 ±(99.9%) 0.067 ns/op [Average]
  Statistics: (min, avg, max) = (8.879, 8.974, 9.331), stdev = 0.090
  Confidence interval (99.9%): [8.907, 9.041]


# Run complete. Total time: 00:02:01

Benchmark                          Mode   Samples        Score  Score error    Units
c.g.w.c.FactoryVsCache.cache       avgt        25        6.866        0.072    ns/op
c.g.w.c.FactoryVsCache.factory     avgt        25        8.974        0.067    ns/op
 */
@State(Scope.Benchmark)
public class FactoryVsCache {
    private static final Map<Method, MethodAccessor> CACHE   = new HashMap<>();
    private static final ReflectionFactory           FACTORY = ReflectionFactory.getReflectionFactory();
    private static Method method;

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + FactoryVsCache.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(25)
                .measurementIterations(25)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    /* @Setup public void setup() throws NoSuchMethodException {
        FactoryVsCache.method = FactoryVsCache.class.getMethod("setup");
        FactoryVsCache.CACHE.put(FactoryVsCache.method, ReflectionFactory.getReflectionFactory().newMethodAccessor(
                FactoryVsCache.method));
    } */

    @Setup public void setup() throws NoSuchMethodException {
        FactoryVsCache.method = FactoryVsCache.class.getMethod("setup");
        FactoryVsCache.CACHE.put(FactoryVsCache.method, null);
    }

    @Benchmark public void cache(Blackhole hole) {
        hole.consume(FactoryVsCache.CACHE.get(FactoryVsCache.method));
    }

    @Benchmark public void factory(Blackhole hole) {
        MethodAccessor accessor = FactoryVsCache.FACTORY.getMethodAccessor(FactoryVsCache.method);
        if (accessor == null)
            hole.consume(FactoryVsCache.FACTORY.newMethodAccessor(FactoryVsCache.method));
    }
}

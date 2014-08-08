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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
# Run progress: 0.00% complete, ETA 00:02:00
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testInsertion
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 43.334 ns/op
# Warmup Iteration   2: 47.989 ns/op
# Warmup Iteration   3: 28.052 ns/op
# Warmup Iteration   4: 27.481 ns/op
# Warmup Iteration   5: 27.370 ns/op
Iteration   1: 24.354 ns/op
Iteration   2: 24.883 ns/op
Iteration   3: 23.336 ns/op
Iteration   4: 23.280 ns/op
Iteration   5: 23.708 ns/op

Result: 23.912 ±(99.9%) 2.661 ns/op [Average]
  Statistics: (min, avg, max) = (23.280, 23.912, 24.883), stdev = 0.691
  Confidence interval (99.9%): [21.251, 26.574]


# Run progress: 8.33% complete, ETA 00:02:44
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testInsertion
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 30.358 ns/op
# Warmup Iteration   2: 24.502 ns/op
# Warmup Iteration   3: 24.861 ns/op
# Warmup Iteration   4: 24.091 ns/op
# Warmup Iteration   5: 23.211 ns/op
Iteration   1: 23.835 ns/op
Iteration   2: 23.098 ns/op
Iteration   3: 23.347 ns/op
Iteration   4: 22.938 ns/op
Iteration   5: 23.358 ns/op

Result: 23.315 ±(99.9%) 1.310 ns/op [Average]
  Statistics: (min, avg, max) = (22.938, 23.315, 23.835), stdev = 0.340
  Confidence interval (99.9%): [22.006, 24.625]


# Run progress: 16.67% complete, ETA 00:02:21
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testInsertion0
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 26.014 ns/op
# Warmup Iteration   2: 24.428 ns/op
# Warmup Iteration   3: 22.553 ns/op
# Warmup Iteration   4: 21.808 ns/op
# Warmup Iteration   5: 22.511 ns/op
Iteration   1: 21.731 ns/op
Iteration   2: 21.852 ns/op
Iteration   3: 21.656 ns/op
Iteration   4: 22.558 ns/op
Iteration   5: 21.701 ns/op

Result: 21.900 ±(99.9%) 1.444 ns/op [Average]
  Statistics: (min, avg, max) = (21.656, 21.900, 22.558), stdev = 0.375
  Confidence interval (99.9%): [20.456, 23.344]


# Run progress: 25.00% complete, ETA 00:02:04
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testInsertion0
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 29.437 ns/op
# Warmup Iteration   2: 23.489 ns/op
# Warmup Iteration   3: 22.253 ns/op
# Warmup Iteration   4: 22.352 ns/op
# Warmup Iteration   5: 21.730 ns/op
Iteration   1: 21.881 ns/op
Iteration   2: 22.038 ns/op
Iteration   3: 21.878 ns/op
Iteration   4: 22.091 ns/op
Iteration   5: 21.734 ns/op

Result: 21.924 ±(99.9%) 0.547 ns/op [Average]
  Statistics: (min, avg, max) = (21.734, 21.924, 22.091), stdev = 0.142
  Confidence interval (99.9%): [21.377, 22.472]


# Run progress: 33.33% complete, ETA 00:01:48
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testRAetrieval
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 28.588 ns/op
# Warmup Iteration   2: 25.598 ns/op
# Warmup Iteration   3: 19.095 ns/op
# Warmup Iteration   4: 19.475 ns/op
# Warmup Iteration   5: 19.016 ns/op
Iteration   1: 19.357 ns/op
Iteration   2: 18.807 ns/op
Iteration   3: 19.420 ns/op
Iteration   4: 18.744 ns/op
Iteration   5: 19.510 ns/op

Result: 19.167 ±(99.9%) 1.397 ns/op [Average]
  Statistics: (min, avg, max) = (18.744, 19.167, 19.510), stdev = 0.363
  Confidence interval (99.9%): [17.770, 20.565]


# Run progress: 41.67% complete, ETA 00:01:34
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testRAetrieval
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 23.291 ns/op
# Warmup Iteration   2: 21.755 ns/op
# Warmup Iteration   3: 17.651 ns/op
# Warmup Iteration   4: 18.575 ns/op
# Warmup Iteration   5: 17.567 ns/op
Iteration   1: 17.813 ns/op
Iteration   2: 17.568 ns/op
Iteration   3: 17.675 ns/op
Iteration   4: 18.574 ns/op
Iteration   5: 17.464 ns/op

Result: 17.819 ±(99.9%) 1.700 ns/op [Average]
  Statistics: (min, avg, max) = (17.464, 17.819, 18.574), stdev = 0.442
  Confidence interval (99.9%): [16.119, 19.519]


# Run progress: 50.00% complete, ETA 00:01:20
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testRAetrieval0
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 27.565 ns/op
# Warmup Iteration   2: 23.332 ns/op
# Warmup Iteration   3: 20.648 ns/op
# Warmup Iteration   4: 19.848 ns/op
# Warmup Iteration   5: 20.425 ns/op
Iteration   1: 19.810 ns/op
Iteration   2: 20.201 ns/op
Iteration   3: 19.751 ns/op
Iteration   4: 19.719 ns/op
Iteration   5: 19.984 ns/op

Result: 19.893 ±(99.9%) 0.771 ns/op [Average]
  Statistics: (min, avg, max) = (19.719, 19.893, 20.201), stdev = 0.200
  Confidence interval (99.9%): [19.122, 20.664]


# Run progress: 58.33% complete, ETA 00:01:07
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testRAetrieval0
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 23.878 ns/op
# Warmup Iteration   2: 21.642 ns/op
# Warmup Iteration   3: 17.920 ns/op
# Warmup Iteration   4: 17.198 ns/op
# Warmup Iteration   5: 17.252 ns/op
Iteration   1: 17.043 ns/op
Iteration   2: 17.163 ns/op
Iteration   3: 16.811 ns/op
Iteration   4: 17.018 ns/op
Iteration   5: 16.694 ns/op

Result: 16.946 ±(99.9%) 0.729 ns/op [Average]
  Statistics: (min, avg, max) = (16.694, 16.946, 17.163), stdev = 0.189
  Confidence interval (99.9%): [16.217, 17.675]


# Run progress: 66.67% complete, ETA 00:00:53
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testRBemoval
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 33.609 ns/op
# Warmup Iteration   2: 29.794 ns/op
# Warmup Iteration   3: 21.643 ns/op
# Warmup Iteration   4: 21.819 ns/op
# Warmup Iteration   5: 21.415 ns/op
Iteration   1: 22.387 ns/op
Iteration   2: 22.001 ns/op
Iteration   3: 21.077 ns/op
Iteration   4: 22.214 ns/op
Iteration   5: 21.363 ns/op

Result: 21.809 ±(99.9%) 2.170 ns/op [Average]
  Statistics: (min, avg, max) = (21.077, 21.809, 22.387), stdev = 0.564
  Confidence interval (99.9%): [19.639, 23.978]


# Run progress: 75.00% complete, ETA 00:00:40
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testRBemoval
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 18.375 ns/op
# Warmup Iteration   2: 17.151 ns/op
# Warmup Iteration   3: 14.720 ns/op
# Warmup Iteration   4: 15.653 ns/op
# Warmup Iteration   5: 16.277 ns/op
Iteration   1: 14.464 ns/op
Iteration   2: 14.947 ns/op
Iteration   3: 14.399 ns/op
Iteration   4: 15.310 ns/op
Iteration   5: 14.908 ns/op

Result: 14.806 ±(99.9%) 1.449 ns/op [Average]
  Statistics: (min, avg, max) = (14.399, 14.806, 15.310), stdev = 0.376
  Confidence interval (99.9%): [13.356, 16.255]


# Run progress: 83.33% complete, ETA 00:00:26
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testRBemoval0
# Parameters: (entries = 10)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 27.599 ns/op
# Warmup Iteration   2: 23.975 ns/op
# Warmup Iteration   3: 21.613 ns/op
# Warmup Iteration   4: 20.853 ns/op
# Warmup Iteration   5: 21.314 ns/op
Iteration   1: 20.691 ns/op
Iteration   2: 21.706 ns/op
Iteration   3: 21.207 ns/op
Iteration   4: 20.697 ns/op
Iteration   5: 21.073 ns/op

Result: 21.075 ±(99.9%) 1.617 ns/op [Average]
  Statistics: (min, avg, max) = (20.691, 21.075, 21.706), stdev = 0.420
  Confidence interval (99.9%): [19.458, 22.692]


# Run progress: 91.67% complete, ETA 00:00:13
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashStructTest.testRBemoval0
# Parameters: (entries = 1000)
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7532 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 17.351 ns/op
# Warmup Iteration   2: 18.073 ns/op
# Warmup Iteration   3: 16.734 ns/op
# Warmup Iteration   4: 14.959 ns/op
# Warmup Iteration   5: 15.678 ns/op
Iteration   1: 14.753 ns/op
Iteration   2: 15.699 ns/op
Iteration   3: 14.460 ns/op
Iteration   4: 15.456 ns/op
Iteration   5: 14.361 ns/op

Result: 14.946 ±(99.9%) 2.312 ns/op [Average]
  Statistics: (min, avg, max) = (14.361, 14.946, 15.699), stdev = 0.600
  Confidence interval (99.9%): [12.634, 17.258]


# Run complete. Total time: 00:02:39

Benchmark                                (entries)   Mode   Samples        Score  Score error    Units
c.g.w.c.HashStructTest.testInsertion            10   avgt         5       23.912        2.661    ns/op
c.g.w.c.HashStructTest.testInsertion          1000   avgt         5       23.315        1.310    ns/op
c.g.w.c.HashStructTest.testInsertion0           10   avgt         5       21.900        1.444    ns/op
c.g.w.c.HashStructTest.testInsertion0         1000   avgt         5       21.924        0.547    ns/op
c.g.w.c.HashStructTest.testRAetrieval           10   avgt         5       19.167        1.397    ns/op
c.g.w.c.HashStructTest.testRAetrieval         1000   avgt         5       17.819        1.700    ns/op
c.g.w.c.HashStructTest.testRAetrieval0          10   avgt         5       19.893        0.771    ns/op
c.g.w.c.HashStructTest.testRAetrieval0        1000   avgt         5       16.946        0.729    ns/op
c.g.w.c.HashStructTest.testRBemoval             10   avgt         5       21.809        2.170    ns/op
c.g.w.c.HashStructTest.testRBemoval           1000   avgt         5       14.806        1.449    ns/op
c.g.w.c.HashStructTest.testRBemoval0            10   avgt         5       21.075        1.617    ns/op
c.g.w.c.HashStructTest.testRBemoval0          1000   avgt         5       14.946        2.312    ns/op
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class HashStructTest {
    private static final Object o     = new Object();
    private static final Object dummy = new Object();
    private static                   Map<Integer, Object> map0;
    private static                   Map<Integer, Object> map;
    @Param({ "10", "1000" }) private int                  entries;

    public static void main(String... args) throws RunnerException {
        HashStructTest.run();
    }

    public static void run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + HashStructTest.class.getSimpleName() + ".*")
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }

    @Benchmark @Fork(1) public static void testInsertion() {
        HashStructTest.map.put(101, HashStructTest.o);
    }

    @Benchmark @Fork(1) public static void testRAetrieval() {
        HashStructTest.map.get(101);
    }

    @Benchmark @Fork(1) public static void testRBemoval() {
        HashStructTest.map.remove(101);
    }

    @Benchmark @Fork(1) public static void testInsertion0() {
        HashStructTest.map0.put(101, HashStructTest.o);
    }

    @Benchmark @Fork(1) public static void testRAetrieval0() {
        HashStructTest.map0.get(101);
    }

    @Benchmark @Fork(1) public static void testRBemoval0() {
        HashStructTest.map0.remove(101);
    }

    @Setup public void setUp() {
        HashStructTest.map = new HashMap<>(this.entries);
        HashStructTest.map0 = new StructBuilder().hash(AbstractHashStruct.HashStrategy.JAVA).buildMap();

        for (int i = 0; i <= this.entries; i++) {
            if (i == 69) {
                HashStructTest.map.put(69, HashStructTest.dummy);
                HashStructTest.map0.put(69, HashStructTest.dummy);

                continue;
            }
            HashStructTest.map.put(i, HashStructTest.o);
            HashStructTest.map0.put(i, HashStructTest.o);
        }
    }
}

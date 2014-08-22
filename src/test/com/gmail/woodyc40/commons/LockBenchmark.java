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

import com.gmail.woodyc40.commons.concurrent.collect.UnsafeLock;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
High contention:

# Run progress: 0.00% complete, ETA 00:00:45
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1000 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.internal
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7540 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 4804343.486 ns/op
# Warmup Iteration   2: 113021.049 ns/op
# Warmup Iteration   3: 100177.644 ns/op
# Warmup Iteration   4: 129708.941 ns/op
# Warmup Iteration   5: 101837.893 ns/op
# Warmup Iteration   6: 106427.664 ns/op
# Warmup Iteration   7: 123314.665 ns/op
# Warmup Iteration   8: 111603.430 ns/op
# Warmup Iteration   9: 131115.263 ns/op
# Warmup Iteration  10: 94031.782 ns/op
Iteration   1: 107920.104 ns/op
Iteration   2: 99775.136 ns/op
Iteration   3: 104230.039 ns/op
Iteration   4: 109895.341 ns/op
Iteration   5: 137123.116 ns/op

Result: 111788.747 ±(99.9%) 56521.975 ns/op [Average]
  Statistics: (min, avg, max) = (99775.136, 111788.747, 137123.116), stdev = 14678.578
  Confidence interval (99.9%): [55266.772, 168310.723]


# Run progress: 33.33% complete, ETA 00:01:22
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1000 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.rlock
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7540 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 143789.030 ns/op
# Warmup Iteration   2: 174143.158 ns/op
# Warmup Iteration   3: 150938.739 ns/op
# Warmup Iteration   4: 141646.450 ns/op
# Warmup Iteration   5: 121695.846 ns/op
# Warmup Iteration   6: 164627.140 ns/op
# Warmup Iteration   7: 127835.619 ns/op
# Warmup Iteration   8: 139403.885 ns/op
# Warmup Iteration   9: 159791.438 ns/op
# Warmup Iteration  10: 122128.750 ns/op
Iteration   1: 125232.535 ns/op
Iteration   2: 117591.505 ns/op
Iteration   3: 135847.246 ns/op
Iteration   4: 127309.736 ns/op
Iteration   5: 161595.736 ns/op

Result: 133515.352 ±(99.9%) 65422.127 ns/op [Average]
  Statistics: (min, avg, max) = (117591.505, 133515.352, 161595.736), stdev = 16989.919
  Confidence interval (99.9%): [68093.225, 198937.478]


# Run progress: 66.67% complete, ETA 00:00:30
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1000 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.synched
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7540 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 534021.843 ns/op
# Warmup Iteration   2: 376392.685 ns/op
# Warmup Iteration   3: 270408.057 ns/op
# Warmup Iteration   4: 357774.440 ns/op
# Warmup Iteration   5: 276116.160 ns/op
# Warmup Iteration   6: 272998.919 ns/op
# Warmup Iteration   7: 260298.420 ns/op
# Warmup Iteration   8: 227263.694 ns/op
# Warmup Iteration   9: 249145.276 ns/op
# Warmup Iteration  10: 279294.025 ns/op
Iteration   1: 323712.432 ns/op
Iteration   2: 214077.767 ns/op
Iteration   3: 202791.548 ns/op
Iteration   4: 242085.775 ns/op
Iteration   5: 243140.058 ns/op

Result: 245161.516 ±(99.9%) 182095.222 ns/op [Average]
  Statistics: (min, avg, max) = (202791.548, 245161.516, 323712.432), stdev = 47289.551
  Confidence interval (99.9%): [63066.294, 427256.739]


# Run complete. Total time: 00:01:22

Benchmark                          Mode   Samples        Score  Score error    Units
c.g.w.c.LockBenchmark.internal     avgt         5   111788.747    56521.975    ns/op
c.g.w.c.LockBenchmark.rlock        avgt         5   133515.352    65422.127    ns/op
c.g.w.c.LockBenchmark.synched      avgt         5   245161.516   182095.222    ns/op

Low contention: (I tried :/)

# Run progress: 0.00% complete, ETA 00:00:45
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 16 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.internal
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7541 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 1574.548 ns/op
# Warmup Iteration   2: 1418.604 ns/op
# Warmup Iteration   3: 1506.841 ns/op
# Warmup Iteration   4: 1503.843 ns/op
# Warmup Iteration   5: 1502.168 ns/op
# Warmup Iteration   6: 1500.739 ns/op
# Warmup Iteration   7: 1506.108 ns/op
# Warmup Iteration   8: 1501.108 ns/op
# Warmup Iteration   9: 1513.049 ns/op
# Warmup Iteration  10: 1501.486 ns/op
Iteration   1: 1504.980 ns/op
Iteration   2: 1500.633 ns/op
Iteration   3: 1506.778 ns/op
Iteration   4: 1502.714 ns/op
Iteration   5: 1501.035 ns/op

Result: 1503.228 ±(99.9%) 10.093 ns/op [Average]
  Statistics: (min, avg, max) = (1500.633, 1503.228, 1506.778), stdev = 2.621
  Confidence interval (99.9%): [1493.134, 1513.321]


# Run progress: 33.33% complete, ETA 00:00:36
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 16 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.rlock
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7541 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 439.131 ns/op
# Warmup Iteration   2: 427.421 ns/op
# Warmup Iteration   3: 341.389 ns/op
# Warmup Iteration   4: 346.292 ns/op
# Warmup Iteration   5: 345.340 ns/op
# Warmup Iteration   6: 345.935 ns/op
# Warmup Iteration   7: 338.954 ns/op
# Warmup Iteration   8: 348.197 ns/op
# Warmup Iteration   9: 349.643 ns/op
# Warmup Iteration  10: 344.106 ns/op
Iteration   1: 347.034 ns/op
Iteration   2: 342.604 ns/op
Iteration   3: 347.450 ns/op
Iteration   4: 343.629 ns/op
Iteration   5: 336.674 ns/op

Result: 343.478 ±(99.9%) 16.730 ns/op [Average]
  Statistics: (min, avg, max) = (336.674, 343.478, 347.450), stdev = 4.345
  Confidence interval (99.9%): [326.748, 360.208]


# Run progress: 66.67% complete, ETA 00:00:18
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 16 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.synched
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7541 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 416.930 ns/op
# Warmup Iteration   2: 423.317 ns/op
# Warmup Iteration   3: 424.620 ns/op
# Warmup Iteration   4: 430.018 ns/op
# Warmup Iteration   5: 425.216 ns/op
# Warmup Iteration   6: 417.369 ns/op
# Warmup Iteration   7: 417.377 ns/op
# Warmup Iteration   8: 439.452 ns/op
# Warmup Iteration   9: 429.828 ns/op
# Warmup Iteration  10: 418.036 ns/op
Iteration   1: 430.961 ns/op
Iteration   2: 416.436 ns/op
Iteration   3: 437.247 ns/op
Iteration   4: 431.931 ns/op
Iteration   5: 418.412 ns/op

Result: 426.997 ±(99.9%) 34.995 ns/op [Average]
  Statistics: (min, avg, max) = (416.436, 426.997, 437.247), stdev = 9.088
  Confidence interval (99.9%): [392.002, 461.992]


# Run complete. Total time: 00:00:55

Benchmark                          Mode   Samples        Score  Score error    Units
c.g.w.c.LockBenchmark.internal     avgt         5     1503.228       10.093    ns/op
c.g.w.c.LockBenchmark.rlock        avgt         5      343.478       16.730    ns/op
c.g.w.c.LockBenchmark.synched      avgt         5      426.997       34.995    ns/op
 */
@State(Scope.Benchmark)
public class LockBenchmark {
    private static final Lock       LOCK  = new ReentrantLock();
    private static final UnsafeLock impl  = new UnsafeLock();
    static               int        count = 0;
    private int counter;

    public static void main(String[] args) throws RunnerException, InterruptedException {
        Options opt = new OptionsBuilder()
                .include(".*" + LockBenchmark.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(10)
                .measurementIterations(5)
                .forks(1)
                .threads(16)
                .build();

        //new org.openjdk.jmh.runner.Runner(opt).run();
        for (int i = 0; i < 10000; i++) LockBenchmark.test();
        //LockBenchmark.test0();
    }

    private static void test() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override public void run() {
                    LockBenchmark.impl.lock();
                    try {
                        LockBenchmark.count++;
                        System.out.println(Thread.currentThread().getName() + " incremented");
                    } finally {
                        LockBenchmark.impl.unlock();
                        latch.countDown();
                    }
                }
            }).start();
        }

        latch.await();
        System.out.println(LockBenchmark.count);
    }

    public static void test0() {
        Executor executor = Executors.newFixedThreadPool(20);
        while (true) {
            executor.execute(new Runnable() {
                @Override public void run() {
                    LockBenchmark.impl.lock();
                    LockBenchmark.impl.unlock();
                }
            });
        }
    }

    @Benchmark public void synched() {
        synchronized (this) {
            this.counter++;
        }
    }

    @Benchmark public void rlock() {
        LockBenchmark.LOCK.lock();
        try {
            this.counter++;
        } finally {
            LockBenchmark.LOCK.unlock();
        }
    }

    @Benchmark public void internal() {
        LockBenchmark.impl.lock();
        try {
            this.counter++;
        } finally {
            LockBenchmark.impl.unlock();
        }
    }
}

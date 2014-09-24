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

import com.gmail.woodyc40.commons.concurrent.collect.InternalLock;
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
# VM options: -Didea.launcher.port=7538 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 5321889.739 ns/op
# Warmup Iteration   2: 5591658.959 ns/op
# Warmup Iteration   3: 5846388.015 ns/op
# Warmup Iteration   4: 5617413.259 ns/op
# Warmup Iteration   5: 5609600.411 ns/op
# Warmup Iteration   6: 5522868.265 ns/op
# Warmup Iteration   7: 5489805.687 ns/op
# Warmup Iteration   8: 5055441.189 ns/op
# Warmup Iteration   9: 4860740.848 ns/op
# Warmup Iteration  10: 4836060.647 ns/op
Iteration   1: 4945175.267 ns/op
Iteration   2: 4932061.010 ns/op
Iteration   3: 4889671.417 ns/op
Iteration   4: 4869044.000 ns/op
Iteration   5: 4881921.017 ns/op

Result: 4903574.542 ±(99.9%) 127662.487 ns/op [Average]
  Statistics: (min, avg, max) = (4869044.000, 4903574.542, 4945175.267), stdev = 33153.542
  Confidence interval (99.9%): [4775912.055, 5031237.029]


# Run progress: 33.33% complete, ETA 00:00:38
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1000 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.rlock
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7538 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 114957.886 ns/op
# Warmup Iteration   2: 117083.471 ns/op
# Warmup Iteration   3: 112837.841 ns/op
# Warmup Iteration   4: 103715.994 ns/op
# Warmup Iteration   5: 104862.812 ns/op
# Warmup Iteration   6: 107943.539 ns/op
# Warmup Iteration   7: 122984.543 ns/op
# Warmup Iteration   8: 104651.622 ns/op
# Warmup Iteration   9: 104107.147 ns/op
# Warmup Iteration  10: 100956.132 ns/op
Iteration   1: 116742.761 ns/op
Iteration   2: 107649.170 ns/op
Iteration   3: 99320.721 ns/op
Iteration   4: 101876.045 ns/op
Iteration   5: 106002.129 ns/op

Result: 106318.165 ±(99.9%) 25776.337 ns/op [Average]
  Statistics: (min, avg, max) = (99320.721, 106318.165, 116742.761), stdev = 6694.033
  Confidence interval (99.9%): [80541.828, 132094.502]


# Run progress: 66.67% complete, ETA 00:00:18
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1000 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.synched
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7538 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 290512.571 ns/op
# Warmup Iteration   2: 228388.985 ns/op
# Warmup Iteration   3: 283107.130 ns/op
# Warmup Iteration   4: 352925.762 ns/op
# Warmup Iteration   5: 395390.346 ns/op
# Warmup Iteration   6: 315462.311 ns/op
# Warmup Iteration   7: 231552.288 ns/op
# Warmup Iteration   8: 255290.788 ns/op
# Warmup Iteration   9: 555723.685 ns/op
# Warmup Iteration  10: 244330.095 ns/op
Iteration   1: 302559.826 ns/op
Iteration   2: 265533.124 ns/op
Iteration   3: 241715.146 ns/op
Iteration   4: 399992.699 ns/op
Iteration   5: 252469.566 ns/op

Result: 292454.072 ±(99.9%) 247795.889 ns/op [Average]
  Statistics: (min, avg, max) = (241715.146, 292454.072, 399992.699), stdev = 64351.805
  Confidence interval (99.9%): [44658.184, 540249.961]


# Run complete. Total time: 00:00:59

Benchmark                          Mode   Samples        Score  Score error    Units
c.g.w.c.LockBenchmark.internal     avgt         5  4903574.542   127662.487    ns/op
c.g.w.c.LockBenchmark.rlock        avgt         5   106318.165    25776.337    ns/op
c.g.w.c.LockBenchmark.synched      avgt         5   292454.072   247795.889    ns/op

========================================================================================================================

Low contention: (I tried :/)

# Run progress: 0.00% complete, ETA 00:00:45
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 16 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.internal
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 44962.824 ns/op
# Warmup Iteration   2: 42524.125 ns/op
# Warmup Iteration   3: 42953.390 ns/op
# Warmup Iteration   4: 42513.677 ns/op
# Warmup Iteration   5: 42138.360 ns/op
# Warmup Iteration   6: 41511.878 ns/op
# Warmup Iteration   7: 42067.380 ns/op
# Warmup Iteration   8: 41998.178 ns/op
# Warmup Iteration   9: 41786.986 ns/op
# Warmup Iteration  10: 43663.736 ns/op
Iteration   1: 43586.092 ns/op
Iteration   2: 41199.565 ns/op
Iteration   3: 43415.475 ns/op
Iteration   4: 43480.490 ns/op
Iteration   5: 42055.925 ns/op

Result: 42747.509 ±(99.9%) 4111.850 ns/op [Average]
  Statistics: (min, avg, max) = (41199.565, 42747.509, 43586.092), stdev = 1067.835
  Confidence interval (99.9%): [38635.659, 46859.360]


# Run progress: 33.33% complete, ETA 00:00:36
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 16 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.rlock
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 375.269 ns/op
# Warmup Iteration   2: 370.218 ns/op
# Warmup Iteration   3: 332.741 ns/op
# Warmup Iteration   4: 333.330 ns/op
# Warmup Iteration   5: 334.279 ns/op
# Warmup Iteration   6: 332.439 ns/op
# Warmup Iteration   7: 338.754 ns/op
# Warmup Iteration   8: 328.317 ns/op
# Warmup Iteration   9: 327.900 ns/op
# Warmup Iteration  10: 328.450 ns/op
Iteration   1: 336.489 ns/op
Iteration   2: 326.934 ns/op
Iteration   3: 332.621 ns/op
Iteration   4: 326.377 ns/op
Iteration   5: 318.048 ns/op

Result: 328.094 ±(99.9%) 26.967 ns/op [Average]
  Statistics: (min, avg, max) = (318.048, 328.094, 336.489), stdev = 7.003
  Confidence interval (99.9%): [301.126, 355.061]


# Run progress: 66.67% complete, ETA 00:00:18
# Warmup: 10 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 16 threads, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.LockBenchmark.synched
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7537 -Didea.launcher.bin.path=/media/A4F1-7AB7/idea-IU-135.1230/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 430.528 ns/op
# Warmup Iteration   2: 426.275 ns/op
# Warmup Iteration   3: 431.495 ns/op
# Warmup Iteration   4: 427.569 ns/op
# Warmup Iteration   5: 427.324 ns/op
# Warmup Iteration   6: 435.306 ns/op
# Warmup Iteration   7: 434.684 ns/op
# Warmup Iteration   8: 429.066 ns/op
# Warmup Iteration   9: 443.702 ns/op
# Warmup Iteration  10: 432.701 ns/op
Iteration   1: 437.834 ns/op
Iteration   2: 441.643 ns/op
Iteration   3: 436.246 ns/op
Iteration   4: 427.875 ns/op
Iteration   5: 432.301 ns/op

Result: 435.180 ±(99.9%) 20.339 ns/op [Average]
  Statistics: (min, avg, max) = (427.875, 435.180, 441.643), stdev = 5.282
  Confidence interval (99.9%): [414.840, 455.519]


# Run complete. Total time: 00:00:55

Benchmark                          Mode   Samples        Score  Score error    Units
c.g.w.c.LockBenchmark.internal     avgt         5    42747.509     4111.850    ns/op
c.g.w.c.LockBenchmark.rlock        avgt         5      328.094       26.967    ns/op
c.g.w.c.LockBenchmark.synched      avgt         5      435.180       20.339    ns/op
 */
@State(Scope.Benchmark)
public class LockBenchmark {
    private static final Lock         LOCK = new ReentrantLock();
    private static final InternalLock impl = new UnsafeLock();
    static  int count;
    private int counter;

    public static void main(String... args) throws RunnerException, InterruptedException {
        Options opt = new OptionsBuilder()
                .include(".*" + LockBenchmark.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(10)
                .measurementIterations(5)
                .forks(1)
                .threads(1000)
                .build();

        new org.openjdk.jmh.runner.Runner(opt).run();
        //for (int i = 0; i < 10000; i++) LockBenchmark.test();
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
        Executor executor = Executors.newFixedThreadPool(2);
        while (true) {
            executor.execute(new Runnable() {
                @Override public void run() {
                    LockBenchmark.impl.lock();
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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

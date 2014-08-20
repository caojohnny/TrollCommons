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

@State(Scope.Benchmark)
public class LockBenchmark {
    private static final Lock       LOCK  = new ReentrantLock();
    private static final UnsafeLock impl  = new UnsafeLock();
    static               int        count = 0;
    private int counter;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + LockBenchmark.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .threads(10)
                .build();

        //new org.openjdk.jmh.runner.Runner(opt).run();
        LockBenchmark.test();
    }

    private static void test() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; true; i++) {
            executor.execute(new Runnable() {
                @Override public void run() {
                    LockBenchmark.impl.lock();
                    try {
                        LockBenchmark.count++;
                    } finally {
                        LockBenchmark.impl.unlock();
                    }
                }
            });
        }

        //System.out.println(LockBenchmark.count);
        //executor.shutdownNow();
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

    @Benchmark public void zInternal() {
        LockBenchmark.impl.lock();
        try {
            this.counter++;
        } finally {
            LockBenchmark.impl.unlock();
        }
    }
}

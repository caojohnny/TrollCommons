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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class ReflectVsNormal {
    private static ReflectVsNormal.Dummy  dummy;
    private static Method method;
    
    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + ReflectVsNormal.class.getSimpleName() + ".*")
                .forks(2)
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(20)
                .measurementIterations(20)
                .build();

        new Runner(opt).run();
    }
    
    @Setup public static void setup() throws NoSuchMethodException {
        ReflectVsNormal.dummy = new ReflectVsNormal.Dummy();
        ReflectVsNormal.method = ReflectVsNormal.dummy.getClass().getDeclaredMethod("doWork");
    }

    @Benchmark public static void NormalInvoke(Blackhole blackhole) {
        blackhole.consume(ReflectVsNormal.dummy.doWork());
    }

    @Benchmark public static void ReflectInvoke(Blackhole blackhole)
            throws InvocationTargetException, IllegalAccessException {
        blackhole.consume(ReflectVsNormal.method.invoke(ReflectVsNormal.dummy));
    }

    private static class Dummy {
        int i;

        public int doWork() {
            return this.i++;
        }
    }
}

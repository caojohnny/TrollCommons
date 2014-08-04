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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS) //TODO
public class SetStructTest {
    private static                   Set<Integer> map0;
    private static                   Set<Integer> map;
    @Param({ "10", "1000" }) private int                  entries;

    public static void main(String... args) throws RunnerException {
        SetStructTest.run();
    }

    public static void run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + SetStructTest.class.getSimpleName() + ".*")
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }

    @Setup public void setUp() {
        SetStructTest.map = new HashSet<>(this.entries);
        SetStructTest.map0 = new StructBuilder().hash(AbstractHashStruct.HashStrategy.JAVA).buildSet();

        for (int i = 0; i <= this.entries; i++) {
            if (i == 69) {
                SetStructTest.map.add(69);
                SetStructTest.map0.add(69);

                continue;
            }
            SetStructTest.map.add(i);
            SetStructTest.map0.add(i);
        }
    }
}

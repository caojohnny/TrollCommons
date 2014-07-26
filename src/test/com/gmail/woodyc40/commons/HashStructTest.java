package com.gmail.woodyc40.commons;

import com.gmail.woodyc40.commons.collect.HashStructMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Setup public void setUp() {
        HashStructTest.map = new HashMap<>(this.entries);
        HashStructTest.map0 = new HashStructMap<>(this.entries);

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

    @Benchmark @Fork(1) public void testInsertion() {
        HashStructTest.map.put(101, HashStructTest.o);
    }

    @Benchmark @Fork(1) public void testRAetrieval() {
        HashStructTest.map.get(101);
    }

    @Benchmark @Fork(1) public void testRBemoval() {
        HashStructTest.map.remove(101);
    }

    @Benchmark @Fork(1) public void testInsertion0() {
        HashStructTest.map0.put(101, HashStructTest.o);
    }

    @Benchmark @Fork(1) public void testRAetrieval0() {
        HashStructTest.map0.get(101);
    }

    @Benchmark @Fork(1) public void testRBemoval0() {
        HashStructTest.map0.remove(101);
    }
}

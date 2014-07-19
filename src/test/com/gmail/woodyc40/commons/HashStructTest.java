package com.gmail.woodyc40.commons;

import com.gmail.woodyc40.commons.collect.HashStructMap;
import org.junit.Test;
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
    @Param({ "10", "1000" }) private int entries;
    private static final Object o     = new Object();
    private static final Object dummy = new Object();

    private        Map<Integer, Object> map;
    private static Map<Integer, Object> map0;

    @Setup public void setUp() {
        this.map = new HashMap<>(this.entries);
        HashStructTest.map0 = new HashStructMap<>(this.entries);

        for (int i = 0; i <= this.entries; i++) {
            if (i == 69) {
                this.map.put(69, HashStructTest.dummy);
                HashStructTest.map0.put(69, HashStructTest.dummy);

                continue;
            }
            this.map.put(i, HashStructTest.o);
            HashStructTest.map0.put(i, HashStructTest.o);
        }
    }

    @Benchmark @Fork(1) public void testInsertion() {
        this.map.put(101, HashStructTest.o);
    }

    @Benchmark @Fork(1) public void testRAetrieval() {
        this.map.get(101);
    }

    @Benchmark @Fork(1) public void testRBemoval() {
        this.map.remove(101);
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

    @Test public void testInsert() {
        this.testInsertion();
        this.testInsertion0();
    }

    @Test public void testRetrieve() {
        this.testRAetrieval();
        this.testRAetrieval0();
    }

    @Test public void testRemove() {
        this.testRBemoval();
        this.testRBemoval0();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
               .include(".*" + HashStructTest.class.getSimpleName() + ".*")
               .warmupIterations(5)
               .measurementIterations(5)
               .build();

        System.out.println(HashStructTest.map0.get(69));

        new Runner(opt).run();
    }
}

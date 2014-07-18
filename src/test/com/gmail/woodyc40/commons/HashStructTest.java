package com.gmail.woodyc40.commons;

import com.gmail.woodyc40.commons.collect.HashStructMap;
import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class HashStructTest {
    private static final HashFunction hasher = Hashing.murmur3_32();

    private static final int    entries = 10;
    private static final Object o       = new Object();

    private static final Map<Integer, Object> map  = new HashMap<>(HashStructTest.entries);
    private static final Map<Integer, Object> map0 = new HashStructMap<>(HashStructTest.entries);

    static {
        for (int i = 0; i <= HashStructTest.entries; i++) {
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
        // Options opt = new OptionsBuilder()
        //        .include(".*" + HashStructTest.class.getSimpleName() + ".*")
        //        .warmupIterations(5)
        //        .measurementIterations(5)
        //        .build();

        // new Runner(opt).run();

        HashStructMap map = new HashStructMap();

        System.out.println(Arrays.toString(new int[] {
                posOf("Niranjan", map.size()),
                posOf("Ananth", map.size()),
                posOf("Niranjan", map.size()),
                posOf("Chandu", map.size()),
        }));
    }

    private static int posOf(Object key, int len) {
        // Implementation:
        // 3 step process -
        // Step 1: Hash the current hashCode of the key
        // Step 2: The value may be negative, convert to unsigned long
        // Step 3: Find the remainder by dividing by the amount of buckets
        // The value of Step 3 is returned as the bucket index

        int hash = HashStructTest.hasher.hashInt(key.hashCode()).asInt();
        long convert = UnsafeProvider.normalize(hash);

        return (int) (convert % ((long) len + 1L));
    }
}

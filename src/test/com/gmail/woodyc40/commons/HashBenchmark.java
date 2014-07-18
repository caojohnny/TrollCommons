package com.gmail.woodyc40.commons;

import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/*

Output:
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7534 -Didea.launcher.bin.path=/media/HP v125w/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.hash
# Parameters: (size = 6999)

# Run progress: 0.00% complete, ETA 00:00:40
# Fork: 1 of 1
# Warmup Iteration   1: 78.376 ns/op
# Warmup Iteration   2: 57.972 ns/op
# Warmup Iteration   3: 54.361 ns/op
# Warmup Iteration   4: 47.675 ns/op
# Warmup Iteration   5: 47.363 ns/op
Iteration   1: 47.055 ns/op
Iteration   2: 46.742 ns/op
Iteration   3: 45.714 ns/op
Iteration   4: 45.968 ns/op
Iteration   5: 45.754 ns/op


Result: 46.247 ±(99.9%) 2.360 ns/op [Average]
  Statistics: (min, avg, max) = (45.714, 46.247, 47.055), stdev = 0.613
  Confidence interval (99.9%): [43.886, 48.607]


# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7534 -Didea.launcher.bin.path=/media/HP v125w/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.hash
# Parameters: (size = 16)

# Run progress: 25.00% complete, ETA 00:00:40
# Fork: 1 of 1
# Warmup Iteration   1: 52.549 ns/op
# Warmup Iteration   2: 39.612 ns/op
# Warmup Iteration   3: 38.074 ns/op
# Warmup Iteration   4: 38.236 ns/op
# Warmup Iteration   5: 37.152 ns/op
Iteration   1: 37.416 ns/op
Iteration   2: 36.825 ns/op
Iteration   3: 37.366 ns/op
Iteration   4: 37.578 ns/op
Iteration   5: 36.149 ns/op


Result: 37.067 ±(99.9%) 2.257 ns/op [Average]
  Statistics: (min, avg, max) = (36.149, 37.067, 37.578), stdev = 0.586
  Confidence interval (99.9%): [34.809, 39.324]


# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7534 -Didea.launcher.bin.path=/media/HP v125w/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.hash0
# Parameters: (size = 6999)

# Run progress: 50.00% complete, ETA 00:00:26
# Fork: 1 of 1
# Warmup Iteration   1: 4.596 ns/op
# Warmup Iteration   2: 4.103 ns/op
# Warmup Iteration   3: 3.660 ns/op
# Warmup Iteration   4: 3.882 ns/op
# Warmup Iteration   5: 3.991 ns/op
Iteration   1: 3.915 ns/op
Iteration   2: 3.344 ns/op
Iteration   3: 3.519 ns/op
Iteration   4: 3.389 ns/op
Iteration   5: 3.977 ns/op


Result: 3.629 ±(99.9%) 1.145 ns/op [Average]
  Statistics: (min, avg, max) = (3.344, 3.629, 3.977), stdev = 0.297
  Confidence interval (99.9%): [2.484, 4.774]


# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7534 -Didea.launcher.bin.path=/media/HP v125w/linux/IntelliJ IDEA/bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.HashBenchmark.hash0
# Parameters: (size = 16)

# Run progress: 75.00% complete, ETA 00:00:13
# Fork: 1 of 1
# Warmup Iteration   1: 4.230 ns/op
# Warmup Iteration   2: 3.584 ns/op
# Warmup Iteration   3: 3.758 ns/op
# Warmup Iteration   4: 3.701 ns/op
# Warmup Iteration   5: 3.353 ns/op
Iteration   1: 3.816 ns/op
Iteration   2: 3.342 ns/op
Iteration   3: 3.330 ns/op
Iteration   4: 3.404 ns/op
Iteration   5: 3.360 ns/op


Result: 3.451 ±(99.9%) 0.794 ns/op [Average]
  Statistics: (min, avg, max) = (3.330, 3.451, 3.816), stdev = 0.206
  Confidence interval (99.9%): [2.656, 4.245]


# Run complete. Total time: 00:00:52

Benchmark                     (size)   Mode   Samples        Score  Score error    Units
c.g.w.c.HashBenchmark.hash      6999   avgt         5       46.247        2.360    ns/op
c.g.w.c.HashBenchmark.hash        16   avgt         5       37.067        2.257    ns/op
c.g.w.c.HashBenchmark.hash0     6999   avgt         5        3.629        1.145    ns/op
c.g.w.c.HashBenchmark.hash0       16   avgt         5        3.451        0.794    ns/op

Run on a Debian box, release 7.6 (Wheezy) 64-bit
Linux Kernal 3.2.0-4-amd64
GNOME 3.4.2

with 2GB RAM and Intel® Pentium(R) Dual CPU E2200 @ 2.20GHz × 2

 */

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class HashBenchmark {
    private static final HashFunction hasher = Hashing.goodFastHash(32);
    private static final int hash = 69;

    @Param({ "6999", "16" }) private int size;

    @Benchmark @Fork(1) public int hash() {
        int hash = HashBenchmark.hasher.hashInt(HashBenchmark.hash).asInt();
        long convert = UnsafeProvider.normalize(hash);

        return (int) (convert % (long) this.size);
    }

    @Benchmark @Fork(1) public int hash0() {
        int h = HashBenchmark.hash;
        h ^= h >>> 20 ^ h >>> 12;
        int now = h ^ h >>> 7 ^ h >>> 4;

        return now & this.size - 1;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + HashBenchmark.class.getSimpleName() + ".*")
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}

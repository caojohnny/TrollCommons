/*
 * This file is part of BukkitCommons.
 *
 * Copyright (C) 2014 AgentTroll
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: woodyc40 (at) gmail (dot) com
 */

package com.gmail.woodyc40.commons.misc;

import com.gmail.woodyc40.commons.collect.ExpandableMapping;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public class Benchmark {
    public static final int           WARP_UP  = 12000;
    public static final int           ITR      = 60000000;
    public static final int           INCR_ITR = 10;
    public static final DecimalFormat FORMAT   = new DecimalFormat("##0.000");

    private final ExpandableMapping<Integer, String> map = new ExpandableMapping<>();
    private ComparisonUnit unit;
    private boolean        increment;

    public ComparisonUnit createComparison() {
        return this.unit = new ComparisonUnit();
    }

    public void run() {
        this.increment = false;
        for (int i = 0; i <= Benchmark.WARP_UP; i++) this.unit.getUnitList().forEach(BenchmarkUnit::run);

        for (int i = 0; i < Benchmark.ITR; i++)
            this.unit.getUnitList().forEach(bench -> {
                long start = System.nanoTime();
                bench.run();
                long nanos = System.nanoTime() - start;

                bench.setRunTime(bench.getRunTime() + nanos);
            });

        for (int i = 0; i <= Benchmark.WARP_UP; i++) this.unit.getUnitList().forEach(BenchmarkUnit::run);

        for (int i = 0; i < Benchmark.ITR; i++)
            this.unit.getUnitList().forEach(bench -> {
                long start = System.nanoTime();
                bench.run();
                long nanos = System.nanoTime() - start;

                bench.setRunTime0(bench.getRunTime0() + nanos);
            });
    }

    public void runIncrementing() {
        this.increment = true;

        long[] total = new long[1];
        for (int i = 0; i <= Benchmark.INCR_ITR; i++) {
            int iterations = (int) StrictMath.pow(2.0, (double) i);
            this.unit.getUnitList().forEach(bench -> {
                for (int j = 0; j <= iterations; j++) {
                    long start = System.nanoTime();
                    bench.run();
                    long nanos = System.nanoTime() - start;
                    total[0] = total[0] + nanos;
                }

                this.map.put(iterations, bench.getName(), total[0]);
            });
        }
    }

    public void printTotal() {
        Table tools = new Table();
        tools.setNames("Trial", "Name", "Iterations", "Elapsed Time");

        if (!this.increment) {
            this.unit.getUnitList().forEach(bench -> {
                Row row = tools.createRow();
                row.setColumn(0, "1")
                   .setColumn(1, bench.getName())
                   .setColumn(2, String.valueOf(Benchmark.ITR))
                   .setColumn(3, String.valueOf(bench.getRunTime()));
            });

            tools.appendSeperator();

            this.unit.getUnitList().forEach(bench -> {
                Row row = tools.createRow();
                row.setColumn(0, "2")
                   .setColumn(1, bench.getName())
                   .setColumn(2, String.valueOf(Benchmark.ITR))
                   .setColumn(3, String.valueOf(bench.getRunTime0()));
            });
        } else {
            Collection<String> list = new ArrayList<>();
            list.add("Iterations");
            this.unit.getUnitList().forEach(u -> {
                list.add(u.getName());
            });

            String[] strings = new String[list.size()];
            Object[] array = list.toArray();
            for (int i1 = 0; i1 < array.length; i1++) {
                Object o = array[i1];
                strings[i1] = (String) o;
            }

            tools.setNames(strings);

            for (int i = 0; i <= Benchmark.INCR_ITR; i++) {
                int pow = (int) StrictMath.pow(2.0, (double) i);
                Row row = tools.createRow();
                row.setColumn(0, String.valueOf(pow));
                list.remove("Iterations");

                for (int j = 1; j < tools.getColumns(); j++) {
                    row.setColumn(j, String.valueOf(this.map.get(pow, this.unit.getUnitList().get(j - 1).getName())));
                }
            }
        }

        tools.print(System.out);
    }

    public void printAverage() {
        Table tools = new Table();
        tools.setNames("Trial", "Name", "Iterations", "Average Time");

        if (!this.increment) {
            this.unit.getUnitList().forEach(bench -> {
                Row row = tools.createRow();
                row.setColumn(0, "1")
                   .setColumn(1, bench.getName())
                   .setColumn(2, String.valueOf(Benchmark.ITR))
                   .setColumn(3, String.valueOf(Benchmark.FORMAT.format(
                           (double) bench.getRunTime() / (double) Benchmark.ITR)));
            });

            tools.appendSeperator();

            this.unit.getUnitList().forEach(bench -> {
                Row row = tools.createRow();
                row.setColumn(0, "2")
                   .setColumn(1, bench.getName())
                   .setColumn(2, String.valueOf(Benchmark.ITR))
                   .setColumn(3, String.valueOf(Benchmark.FORMAT.format(
                           (double) bench.getRunTime0() / (double) Benchmark.ITR)));
            });
        } else {
            Collection<String> list = new ArrayList<>();
            list.add("Iterations");
            this.unit.getUnitList().forEach(u -> {
                list.add(u.getName());
            });

            String[] strings = new String[list.size()];
            Object[] array = list.toArray();
            for (int i1 = 0; i1 < array.length; i1++) {
                Object o = array[i1];
                strings[i1] = (String) o;
            }

            tools.setNames(strings);

            for (int i = 0; i <= Benchmark.INCR_ITR; i++) {
                int pow = (int) StrictMath.pow(2.0, (double) i);
                Row row = tools.createRow();
                row.setColumn(0, String.valueOf(pow));
                list.remove("Iterations");

                for (int j = 1; j < tools.getColumns(); j++) {
                    row.setColumn(j, String.valueOf(Benchmark.FORMAT.format(
                            (double) (long) this.map.get(pow, this.unit.getUnitList().get(j - 1).getName()) /
                            (double) pow
                    )));
                }
            }
        }

        tools.print(System.out);
    }
}


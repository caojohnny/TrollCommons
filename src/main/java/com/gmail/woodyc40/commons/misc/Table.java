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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

public class Table {
    private final Collection<Row> rowEntries = new ArrayList<>();
    int[] max = new int[0];
    int columns;
    private String[] names = { };

    public Row createRow() {
        Row row = new RowEntry(this);
        this.rowEntries.add(row);
        return row;
    }

    public void appendSeperator() {
        this.rowEntries.add(new Line());
    }

    public void setNames(String... names) {
        this.names = names.clone();

        this.max = new int[this.columns = names.length];
    }

    public int getColumns() {
        return this.columns;
    }

    public void print(PrintStream stream) {
        StringBuilder format = new StringBuilder();
        format.append('|');

        StringBuilder dashes = new StringBuilder();

        for (int i1 = 0; i1 < this.max.length; i1++) {
            int maximum = this.max[i1] > this.names[i1].length() ? this.max[i1] : this.names[i1].length();
            format.append("%-").append(maximum).append('s');
            format.append('|');

            if (i1 == 0) dashes.append('+');
            for (int i = 0; i < maximum; i++) {
                dashes.append('-');
                if (i == maximum - 1) dashes.append('+');
            }
        }

        stream.println(dashes);
        stream.printf(format.toString(), this.names);
        System.out.println();
        stream.println(dashes);

        for (Row rowEntry : this.rowEntries) {
            if (rowEntry instanceof Line) {
                stream.println(dashes);
                continue;
            }
            stream.printf(format.toString(), rowEntry.getEntries());
            stream.print("\n");
        }
        stream.println(dashes);
    }
}
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

package com.gmail.woodyc40.commons.misc;

import lombok.Getter;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ASCII table wrapper that can be formatted and printed out to the console
 *
 * @author AgentTroll
 * @version 1.0
 */
public class Table {
    /** Table data rows */
    private final Collection<Row> rowEntries = new ArrayList<>();
    /** Represents the maximum lines to append in ASCII */
    int[] max = new int[0];
    /** Columns of data to support */
    @Getter int columns;
    /** The titles of the table */
    private String[] names = { };

    /**
     * Makes a new row and appends it to the row collection
     *
     * @return the new row created
     */
    public Row createRow() {
        Row row = new RowEntry(this);
        this.rowEntries.add(row);
        return row;
    }

    /**
     * Appends a line to the table
     */
    public void appendSeperator() {
        this.rowEntries.add(new Line());
    }

    /**
     * The titles of each column on the table
     * <p/>
     * <p/>
     * Unlimited slots are available, in fact, the amount of columns are set by this method.
     *
     * @param names the titles
     */
    public void setNames(String... names) {
        this.names = names.clone();

        this.max = new int[this.columns = names.length];
    }

    /**
     * Prints out the formatted table to the stream
     *
     * @param stream the stream to print out to
     */
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
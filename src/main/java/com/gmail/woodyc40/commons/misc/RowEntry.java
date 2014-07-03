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

public class RowEntry implements Row {
    private final String[] entries;
    private final Table    table;

    public RowEntry(Table table) {
        this.table = table;
        this.entries = new String[table.columns];
    }

    @Override public Row setColumn(int column, String entry) {
        this.entries[column] = entry;

        int max = this.table.max[column];
        if (entry.length() > max)
            this.table.max[column] = entry.length();
        return this;
    }

    @Override public String[] getEntries() {
        return this.entries;
    }
}

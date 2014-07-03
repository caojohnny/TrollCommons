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

public class BenchmarkUnit implements Runnable {
    private final String   name;
    private final Runnable runnable;
    private       long     runTime;
    private       long     runTime0;

    public BenchmarkUnit(String name, Runnable runnable) {
        this.name = name;
        this.runnable = runnable;
    }

    public final String getName() {
        return this.name;
    }

    public final long getRunTime() {
        return this.runTime;
    }

    public final void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public final long getRunTime0() {
        return this.runTime0;
    }

    public final void setRunTime0(long runTime) {
        this.runTime0 = runTime;
    }

    @Override
    public void run() {
        this.runnable.run();
    }
}

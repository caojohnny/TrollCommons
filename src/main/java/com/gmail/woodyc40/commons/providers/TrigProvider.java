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

package com.gmail.woodyc40.commons.providers;

import java.util.HashMap;
import java.util.Map;

/**
 * Faster hashtable look ups for trig functions sin, cos, and tan
 *
 * @author AgentTroll
 * @version 1.0
 */
public final class TrigProvider {
    /**
     * Sin values
     */
    private static final Map<Double, Double> sin = new HashMap<>();
    /**
     * Cosine values
     */
    private static final Map<Double, Double> cos = new HashMap<>();
    /**
     * Tangent values
     */
    private static final Map<Double, Double> tan = new HashMap<>();

    static {
        for (int temp = 0; temp <= 36000; temp++) {
            double d = (double) (temp / 100);
            TrigProvider.sin.put(Double.valueOf(d), Double.valueOf(StrictMath.sin(d)));
            TrigProvider.cos.put(Double.valueOf(d), Double.valueOf(StrictMath.cos(d)));
            TrigProvider.tan.put(Double.valueOf(d), Double.valueOf(StrictMath.tan(d)));
        }
    }

    private TrigProvider() {}

    /**
     * Executes lookup for sine of value
     *
     * @param val the value to sin for
     * @return the sine of val
     */
    public static double sin(double val) {
        return TrigProvider.sin.get(Double.valueOf(val));
    }

    /**
     * Executes lookup for tangent of value
     *
     * @param val the value to tan for
     * @return the tangent of val
     */
    public static double cos(double val) {
        return TrigProvider.cos.get(Double.valueOf(val));
    }

    /**
     * Executes lookup for cosine of value
     *
     * @param val the value to cos for
     * @return the cosine of val
     */
    public static double tan(double val) {
        return TrigProvider.tan.get(Double.valueOf(val));
    }
}

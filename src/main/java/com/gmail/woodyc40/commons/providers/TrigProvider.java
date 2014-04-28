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
public class TrigProvider {
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
            double d = temp / 100;
            sin.put(Double.valueOf(d), Double.valueOf(Math.sin(d)));
            cos.put(Double.valueOf(d), Double.valueOf(Math.cos(d)));
            tan.put(Double.valueOf(d), Double.valueOf(Math.tan(d)));
        }
    }

    /**
     * Executes lookup for sine of value
     *
     * @param val the value to sin for
     * @return the sine of val
     */
    public static double sin(double val) {
        double ret = 0.0;
        ret = sin.get(Double.valueOf(val));
        return ret;
    }

    /**
     * Executes lookup for tangent of value
     *
     * @param val the value to tan for
     * @return the tangent of val
     */
    public static double cos(double val) {
        double ret = 0.0;
        ret = cos.get(Double.valueOf(val));
        return ret;
    }

    /**
     * Executes lookup for cosine of value
     *
     * @param val the value to cos for
     * @return the cosine of val
     */
    public static double tan(double val) {
        double ret = 0.0;
        ret = tan.get(Double.valueOf(val));
        return ret;
    }
}

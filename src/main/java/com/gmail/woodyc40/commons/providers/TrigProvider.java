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

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

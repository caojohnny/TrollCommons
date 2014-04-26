package com.gmail.woodyc40.commons.reflection.asm;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility to get JVM letter representations for primitives
 * 
 * @author AgentTroll
 * @version 1.0
 */ 
public class Primitives {
    private Primitives() {}
    
    /**
     * The primitive descriptors
     */ 
    private static final Map<Class<?>, String> DESC_MAP = new HashMap<>();

    static {
        DESC_MAP.put(boolean.class, "Z");
        DESC_MAP.put(byte.class, "B");
        DESC_MAP.put(char.class, "C");
        DESC_MAP.put(double.class, "D");
        DESC_MAP.put(float.class, "F");
        DESC_MAP.put(int.class, "I");
        DESC_MAP.put(long.class, "J");
        DESC_MAP.put(short.class, "S");

        DESC_MAP.put(void.class, "V");
    }

    /**
     * Gets the descriptor associated with the primitive type
     * 
     * @param primitive the <code>class</code> representing a primitive type
     * @return the JVM descriptor for the primitive, or <code>null</code> if 
     *         not registered or not a primitive type
     */ 
    public static String getDesc(Class<?> primitive) {
        return DESC_MAP.get(primitive);
    }
}

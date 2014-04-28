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

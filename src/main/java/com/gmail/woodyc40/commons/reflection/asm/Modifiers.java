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

/**
 * Modifier flags for fields, methods, and constructors
 *
 * @author AgentTroll
 * @version 1.0
 */
public class Modifiers {
    // Fields
    public static final int ACC_VOLATILE  = 0x0040;
    public static final int ACC_TRANSIENT = 0x0080;

    // Multiple
    public static final int ACC_PUBLIC    = 0x0001;
    public static final int ACC_FINAL     = 0x0010;
    public static final int ACC_ABSTRACT  = 0x0400;
    public static final int ACC_SYNTHETIC = 0x1000;
    public static final int ACC_ENUM      = 0x4000;

    // Methods
    public static final int ACC_PRIVATE      = 0x0002;
    public static final int ACC_PROTECTED    = 0x0004;
    public static final int ACC_STATIC       = 0x0008;
    public static final int ACC_SYNCHRONIZED = 0x0020;
    public static final int ACC_BRIDGE       = 0x0040;
    public static final int ACC_VARARGS      = 0x0080;
    public static final int ACC_NATIVE       = 0x0100;
    public static final int ACC_STRICT       = 0x0800;


    // Classes
    public static final int ACC_SUPER      = 0x0020;
    public static final int ACC_INTERFACE  = 0x0200;
    public static final int ACC_ANNOTATION = 0x2000;
}

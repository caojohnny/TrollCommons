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

import com.gmail.woodyc40.commons.reflection.asm.sun.ClassFileAssembler;

/**
 * The bytecode representation of a field in the JVM
 * 
 * @author AgentTroll
 * @version 1.0
 */ 
public class BytecodeField {
    /**
     * The access modifiers of the field, specified by {@link com.gmail.woodyc40.commons.reflection.asm.Modifiers}
     */ 
    private final int    access;
    /**
     * The declared title of the field
     */ 
    private final String name;
    /**
     * The descriptor of the field, for internal use only. Can be used to decode <code>class</code> type of 
     * the field
     */ 
    private final String desc;

    /**
     * Creates a new instance of a field specifier.
     * 
     * @param name the name of the field to be declared by the bytecode dump
     * @param access the access modifiers of the field, specified by 
     *        {@link com.gmail.woodyc40.commons.reflection.asm.Modifiers}
     * @param type the type of field to be assigned a value, for example:
     *        <code>byte vec;</code> would be <code>byte.class</code>
     */ 
    public BytecodeField(String name, int access, Class<?> type) {
        this.access = access;
        this.name = name;
        this.desc = Descriptors.descField(type);
    }

    /**
     * Adds the field to the bytecode dump of a represented builder
     * 
     * @param bytecode the builder to add the field bytecode to 
     */ 
    public void add(ClassBytecode bytecode) {
        ClassFileAssembler assembler = bytecode.getAssembler();
        Pool pool = bytecode.getConstantPool();

        assembler.emitShort((short) access); // Access
        assembler.emitShort((short) pool.newUTF(name)); // Name
        assembler.emitShort((short) pool.newUTF(desc)); // Descriptor
        assembler.emitShort((short) 0); // Attributes // TODO attributes for code ? fields = value
    }
}

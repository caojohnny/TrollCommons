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
 * The bytecode representation of a method in the JVM
 * 
 * @author AgentTroll
 * @version 1.0
 */ 
public class BytecodeMethod {
    /**
     * Access flags for the method specified by {@link com.gmail.woodyc40.commons.reflection.asm.Modifiers}
     */ 
    private final int    access;
    /**
     * The declared name of the method
     */ 
    private final String name;
    /**
     * The method descriptor, for internal use only. Can be used to acquire method parameters and return type
     */ 
    private final String desc;

    /**
     * The code representing the call stack for this method
     */ 
    private Pool.Attr attr;

    /**
     * Builds a new instnace of the bytecode representation for a method
     * 
     * @param methodName the declared name of the method
     * @param access the access flags for the method 
     *        specified by {@link com.gmail.woodyc40.commons.reflection.asm.Modifiers}
     * @param ret the return type for the method, for example
     *        <code>public void foo() {}</code> would be <code>void.class</code>
     * @param params the parameters for the method, can be an empty array for no parameters
     */ 
    public BytecodeMethod(String methodName, int access, Class<?> ret, Class[] params) {
        this.access = access;
        this.name = methodName;
        this.desc = Descriptors.descMethod(ret, params);
    }

    /**
     * Associates a bytecode attribute for the method code
     * 
     * @param attr the attribute to add representing the method call stack
     */ 
    public void addCode(Pool.CodeAttr attr) {
        this.attr = attr;
    }

    /**
     * Addes the method to a bytecode dump builder
     * 
     * @param bytecode the builder to add the method to
     */ 
    public void add(ClassBytecode bytecode) {
        ClassFileAssembler assembler = bytecode.getAssembler();
        Pool pool = bytecode.getConstantPool();

        assembler.emitShort((short) access); // Access
        assembler.emitShort((short) pool.newUTF(name)); // Name
        assembler.emitShort((short) pool.newUTF(desc)); // Descriptor
        assembler.emitShort((short) (attr != null ? 1 : 0)); // Attributes
        if (attr != null)
            attr.toData(assembler);
    }
}

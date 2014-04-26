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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates dynamic classes for unsafe cast
 *
 * @author AgentTroll
 * @version 1.0
 */
public class ClassBytecode {
    private ClassBytecode() {}

    // Class schema:
    // Magic header  - 0xCAFEBABE int
    // Major version - 51         short
    // Minor version - 0          short
    // Constant pool -            amount (short), all data types (tag (short), value (type write))
    // Access        -            short
    // Class         -            short
    // Superclass    -            short
    //
    // Interfaces    -            short
    // Interface val -            short
    // Fields        -            short
    // Field val     -            Access (short), Name (short index at constant pool),
    // Descriptor (short index at constant pool), 0 (short attr)
    // Methods       -            short
    // Method val    -            Access (short), Name (short index at constant pool),
    // Descriptor (short index at constant pool), 0 (short attr)
    // Attributes    -            short
    // Atribute val  -            short name, short data amount, byte data array loop

    /**
     * The assembly utility from the <code>sun.reflect package</code> 
     */ 
    private final ClassFileAssembler   asm          = new ClassFileAssembler();
    /**
     * The bytecoded constant pool used to reference vars from bytecode
     */ 
    private final Pool                 constantPool = new Pool();
    /**
     * The fields of this <code>class</code>
     */ 
    private final List<BytecodeField>  fields       = new ArrayList<>();
    /**
     * The methods of this <code>class</code>
     */ 
    private final List<BytecodeMethod> methods      = new ArrayList<>();

    /**
     * The declared name of the <code>class</code>
     */ 
    private String   className;
    /**
     * The superclass of this <code>class</code>
     */ 
    private Class<?> superclass;
    /**
     * The interfaces <code>implemented</code> by this <code>class</code>
     */ 
    private Class[]  interfaces;
    /**
     * Whether or not this <code>class</code> is an <code>interface</code>
     */ 
    private boolean  isInterface;

    /**
     * Creates a new instance of the bytecode builder
     * 
     * @param className the name of the <code>class</code>
     * @param superclass the concrete <code>class</code> that will be <code>extend</code>ed
     *        by this <code>class</code>. {@link java.lang.Object}.<code>class</code> if none
     * @param interfaces the concrete <code>inteface</code>s that will be <code>implemented</code>
     *        by this <code>class</code>, an empty <code>class</code> array if none
     * @param isInterface whether or not this <code>class</code> is an interface
     */ 
    public ClassBytecode(String className, Class<?> superclass, Class[] interfaces,
                         boolean isInterface) {
        asm.emitMagicAndVersion(); // Magic + version header

        this.className = className;
        this.superclass = superclass;
        this.interfaces = interfaces;
        this.isInterface = isInterface;
    }

    /**
     * Gets the assembler used to emit bytecode to the builder
     * 
     * @return the {@link com.gmail.woodyc40.commons.reflection.asm.sun.ClassFileAssembler} 
     *         associated with this builder
     */ 
    public ClassFileAssembler getAssembler() {
        return this.asm;
    }

    /**
     * Gets the constant listings in the pool used to hold local vars
     * 
     * @return the constant pool associated with the builder
     */ 
    public Pool getConstantPool() {
        return this.constantPool;
    }

    /**
     * Returns a <code>byte</code> array representing the built class
     * 
     * <p>
     * Array based in order to ensure compatibility
     * 
     * @return the <code>byte</code>s representing the bytecode of the built <code>class</code>
     */ 
    public byte[] writeClassCode() {
        try {
            constantPool.writeData(asm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isInterface)
            asm.emitShort((short) (Modifiers.ACC_PUBLIC | Modifiers.ACC_INTERFACE | Modifiers.ACC_ABSTRACT));
        else asm.emitShort((short) (Modifiers.ACC_PUBLIC | Modifiers.ACC_SUPER));                    // Access

        asm.emitShort((short) constantPool.newClassInfo(className)); // Class name
        asm.emitShort((short) constantPool.newClassInfo(superclass.getName())); // Superclass name

        asm.emitShort((short) interfaces.length);              // Interfaces
        for (Class<?> face : interfaces)
            asm.emitShort((short) constantPool.newClassInfo(face.getName()));

        asm.emitShort((short) methods.size());    // Fields
        for (BytecodeField field : fields)
            field.add(this);

        asm.emitShort((short) methods.size());    // Methods
        for (BytecodeMethod method : methods)
            method.add(this);

        asm.emitShort((short) 1);   // Attributes

        String fnlName = "";
        int index = className.lastIndexOf('.');
        if (index >= 0)
            fnlName = className.substring(index + 1) + ".java";

        Pool.Attr attr = new Pool.SourceAttr(constantPool, fnlName);
        attr.toData(asm);

        return asm.getData().getData();
    }

    /**
     * Adds a field to the builder, uses params of the constructor of the representing field
     * 
     * <p>
     * See {@link com.gmail.woodyc40.commons.reflection.asm.BytecodeField} for more details
     * 
     * @param fieldName the declared name of the field
     * @param access the access flags to declare the field as
     * @param type the type of field the declaration will hold
     */ 
    public void addField(String fieldName, int access, Class<?> type) {
        fields.add(new BytecodeField(fieldName, access, type));
    }

    /**
     * Adds a method to the builder, uses a built object in order to ensure that all properties
     * of the method have been modified by the builder prior to addition to the method list
     * 
     * @param method the fully built method instance to add to the method listings
     */ 
    public void addMethod(BytecodeMethod method) {
        methods.add(method);
    }
}

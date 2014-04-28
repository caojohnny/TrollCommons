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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a constant pool in a bytecoded <code>class</code>
 * 
 * @author AgentTroll
 * @version 1.0
 */ 
public class Pool {
    /**
     * {@link java.util.List} of constants held by the pool
     */ 
    private final List<ConstantImpl<?>> constantList = new ArrayList<>();

    /**
     * The template for a UTF8 constant
     */ 
    private final Constant<String> utfConst = new Constant<String>(1) {
        @Override
        void out(DataOutputStream stream, String string) throws IOException {
            stream.writeUTF(string);
        }
    };

    /**
     * The template for an int constant
     */ 
    private final Constant<Integer> classInfoConst = new Constant<Integer>(7) { // Special case: int takes position
        // on constantpool
        @Override
        void out(final DataOutputStream stream, final Integer integer) throws IOException {
            stream.writeShort(integer.intValue());
        }
    };

    /**
     * Pushes a constant implementation to the constant pool
     * 
     * @param impl the impementation to push
     * @return the index of the latest push size of the pool
     */ 
    private int toList(ConstantImpl<?> impl) {
        constantList.add(impl);
        return constantList.size();
    }

    /**
     * Pushes a new UTF8 string to the constant pool
     * 
     * @param utf the string to add to the pool
     * @return the index of the string in the pool
     */ 
    public int newUTF(String utf) {
        return toList(new ConstantImpl<>(utfConst, utf));
    }

    /**
     * Addes <code>class</code> info referenced by the bytecode builder to the pool
     * 
     * @param className the fully qualified name of the <code>class</code>
     * @return the index of the info in the constant pool
     */ 
    public int newClassInfo(String className) {
        int name = newUTF(className.replace(".", "/"));
        return toList(new ConstantImpl<>(classInfoConst, Integer.valueOf(name)));
    }
    
    /**
     * Writes the constant pool into the bytecode assembler
     * 
     * @param assembler the assembler used by the bytecode builder to add code to the file
     */ 
    public void writeData(ClassFileAssembler assembler) throws IOException {
        assembler.emitShort((short) constantList.size());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream stream0 = new DataOutputStream(stream);
        for (ConstantImpl<?> impl : constantList) {
            assembler.emitShort((short) impl.template.tag);
            impl.write(stream0);
        }
        stream0.close();

        for (byte b : stream.toByteArray())
            assembler.emitByte(b);
        stream.close();
    }

    /**
     * Value holder for Constant serializer templates
     * 
     * @param <T> the type of template value to hold
     * @author AgentTroll
     * @version 1.0
     */ 
    private class ConstantImpl<T> {
        /**
         * The value held by the implementation
         */ 
        private final T           t;
        /**
         * The template used to serialize the data held 
         */
        private final Constant<T> template;

        /**
         * Creates a new value holder off of a template
         * 
         * @param template the serializer reference to assist with sending data
         * @param t the value to hold
         */ 
        public ConstantImpl(Constant<T> template, T t) {
            this.t = t;
            this.template = template;
        }

        /**
         * Writes the data held by this <code>class</code>
         */ 
        public void write(DataOutputStream output) throws IOException {
            template.write(output, this.t);
        }
    }

    /**
     * A serializer template used to send data to a <code>byte</code> stream
     * 
     * @param <T> the value to hold
     * @author AgentTroll
     * @version 1.0
     */ 
    private abstract class Constant<T> {
        /**
         * JVM <code>int</code> that signifies the type of Constant in the pool
         */ 
        final int tag;

        /**
         * Builds a new template based off of the JVM tag for a specifed tag
         * 
         * @param tag the JVM <code>int</code> used to identify the constant type
         */ 
        public Constant(int tag) {
            this.tag = tag;
        }

        /**
         * Writes the data of implementations to a stream
         * 
         * <p>
         * Differs from the out method since this also writes the tag held by the 
         * constant identifier
         * 
         * @param stream the writer to use in serialization
         * @param t the object to put to stream
         */ 
        public void write(DataOutputStream stream, T t) throws IOException {
            stream.writeByte((byte) this.tag);
            out(stream, t);
        }

        /**
         * Template override specific method to write data represented by the holder
         * to the stream
         * 
         * @param stream the writer to use in serialization
         * @param t the object to put to stream
         */ 
        abstract void out(DataOutputStream stream, T t) throws IOException;
    }

    /**
     * Weak access to data attribute writers
     * 
     * @author AgentTroll
     * @version 1.0
     */ 
    public interface Attr {
        /**
         * Assemble the data into the bytecode writer
         * 
         * @param assembler the data writer held by the bytecode builder
         */ 
        public void toData(ClassFileAssembler assembler);
    }

    /**
     * Code attribute used to identify the <code>class</code> source name
     * 
     * @author AgentTroll
     * @version 1.0
     */ 
    public static class SourceAttr implements Attr {
        /**
         * The JVM tag name for this attribute
         */ 
        private final String tag = "SourceFile";
        /**
         * The temporary pool source to write the attribute data to
         */ 
        private final Pool   def;
        /**
         * The data held for the attribute
         */ 
        private final byte[] data;

        /**
         * Creates a new source attribution for a <code>class</code> bytecode
         * 
         * @param pool the constant pool used by the bytecode builder
         * @param name the source name of the <code>class</code>, usually
         *        the unqualified name followed by <code>.java</code>
         */ 
        public SourceAttr(Pool pool, String name) {
            this.def = pool;
            int index = pool.newUTF(name);
            byte[] bvalue = new byte[2];
            bvalue[0] = (byte) (index >>> 8);
            bvalue[1] = (byte) index;
            data = bvalue;
        }

        /**
         * {@inheritDoc}
         */ 
        public void toData(ClassFileAssembler assembler) {
            assembler.emitShort((short) def.newUTF(tag));
            assembler.emitShort((short) data.length);
            for (byte b : data)
                assembler.emitByte(b);
        }
    }

    public static class CodeAttr implements Attr {
        /**
         * The JVM tag name for this attribute
         */ 
        private final String tag = "Code";
        /**
         * The temporary pool source to write the attribute data to
         */ 
        private final Pool   def;
        /**
         * The data held for the attribute
         */ 
        private final byte[] data;

        /**
         * As found in the JVM spec:
         * 
         * " the maximum depth of the operand stack of this method (§2.6.2) 
         * at any point during execution of the method."
         */ 
        private final int maxStack;
        /**
         * As found in the JVM spec:
         * 
         * " the number of local variables in the local variable array allocated 
         * upon invocation of this method (§2.6.1), including the local variables 
         * used to pass parameters to the method on its invocation"
         * 
         * <p>
         * The greatest local variable index for a value of type long or double is 
         * max_locals - 2. The greatest local variable index for a value of any other 
         * type is max_locals - 1. (JVM spec)
         */ 
        private final int maxLocals;

        /**
         * Builds a new code attribution for use with method body call stacks
         * 
         * @param pool the constant pool used by the bytecode builder
         * @param stack refer to max_stack of the 
         *        <a href=http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.3>JVM spec</a>
         * @param locals refer to max_locals of the 
         *        <a href=http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.3>JVM spec</a>
         * @param assembler the pre-definded bytecode builder with loaded opcode calls
         *        to define the method call
         */ 
        public CodeAttr(Pool pool, int stack, int locals, ClassFileAssembler assembler) {
            this.def = pool;

            maxStack = stack;
            maxLocals = locals;

            data = assembler.getData().getData();
        }

        /**
         * {@inheritDoc}
         */
        public void toData(ClassFileAssembler assembler) {
            assembler.emitShort((short) def.newUTF(tag));

            int len = (18 + data.length + 0 * 8 /* Exception table length */
                    + 0/* Attribute length */) - 6;
            assembler.emitShort((short) len);
            assembler.emitShort((short) maxStack);
            assembler.emitShort((short) maxLocals);
            assembler.emitShort((short) data.length);
            for (byte b : data)
                assembler.emitByte(b);
            assembler.emitShort((short) 0); // Exception table length
            assembler.emitShort((short) 0); // Attributes
            for (byte b : data)
                assembler.emitByte(b);
        }
    }
}

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

package com.gmail.woodyc40.commons.instrument.refs;

import com.gmail.woodyc40.commons.misc.Pair;
import com.gmail.woodyc40.commons.reflection.MethodManager;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;
import com.gmail.woodyc40.commons.reflection.chain.ReflectionChain;
import com.gmail.woodyc40.commons.reflection.impl.ReflectAccess;
import com.gmail.woodyc40.commons.reflection.impl.ReflectionCache;
import lombok.*;

import java.io.*;

/**
 * The reference to a constant pool entry
 *
 * @author AgentTroll
 * @version 1.0
 */
@Getter @Setter
public class ConstantRef {
    /** The tag getter of javassist constinfo */
    private static final MethodManager<Object, Integer> tag = ReflectAccess.accessMethod(
            ReflectionTool.forMethod("getTag", ReflectionCache.getClass("javassist.bytecode.ConstPool$ConstInfo"))
    );
    /** The index in the constant pool */
    private final   int              index;
    /** Type */
    private         ConstantRef.Type type;
    /** Value (depends on the type, see the Type for details on what the value should be */
    @Setter private Object           value;
    private         Class<?>         constInfo;

    /**
     * Builds a reference of the javassist constinfo
     *
     * @param constInfo the javassist constant pool entry
     * @param index     the index of the constant pool entry
     */
    public ConstantRef(Object constInfo, int index) {
        this.constInfo = constInfo.getClass();
        this.index = index;
        switch (ConstantRef.tag.invoke(constInfo)) {
            case 0:
                this.type = ConstantRef.Type.PADDING;
                break;
            case 1:
                this.type = ConstantRef.Type.UTF8;
                break;
            case 3:
                this.type = ConstantRef.Type.INTEGER;
                break;
            case 4:
                this.type = ConstantRef.Type.FLOAT;
                break;
            case 5:
                this.type = ConstantRef.Type.LONG;
                break;
            case 6:
                this.type = ConstantRef.Type.DOUBLE;
                break;
            case 7:
                this.type = ConstantRef.Type.CLASS;
                break;
            case 8:
                this.type = ConstantRef.Type.STRING;
                break;
            case 9:
                this.type = ConstantRef.Type.FIELD;
                break;
            case 10:
                this.type = ConstantRef.Type.METHOD;
                break;
            case 11:
                this.type = ConstantRef.Type.INTERFACE_METHOD;
                break;
            case 12:
                this.type = ConstantRef.Type.NAME_AND_TYPE;
                break;
        }
    }

    /**
     * Outputs the data to the stream
     *
     * @param stream the stream to write to
     * @throws IOException if the write to the stream errors
     */
    public void write(DataOutputStream stream) throws IOException {
        stream.write(this.type.writeData(this.value));
    }

    public Object toConstInfo() {
        return new ReflectionChain(this.constInfo).contruct().
                construct(int.class).param(this.index).creator().create()
                                                  .reflect();
    }

    // TODO verifier

    /**
     * The type of constant this reference represents
     *
     * @author AgentTroll
     * @version 1.0
     */
    public enum Type {
        /** Padding info after long and double */
        PADDING(0) {
            @Override public void write(DataOutputStream stream, Object value) {
            }
        },
        /** String type in UTF8 */
        UTF8(1) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                stream.writeUTF((String) value);
            }
        },
        /** Integer type */
        INTEGER(3) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                stream.writeInt((int) value);
            }
        },
        /** Float type */
        FLOAT(4) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                stream.writeFloat((float) value);
            }
        },
        /** Long type */
        LONG(5) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                stream.writeLong((long) value);
            }
        },
        /** Double type */
        DOUBLE(6) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                stream.writeDouble((double) value);
            }
        },
        /**
         * Class type
         * <p/>
         * <p/>
         * value is the index of the class name, in short
         */
        CLASS(7) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                stream.writeShort((int) (short) value);
            }
        },
        /**
         * String type
         * <p/>
         * <p/>
         * value is the index of the string, in short
         */
        STRING(8) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                stream.writeShort((int) (short) value);
            }
        },
        /**
         * Fieldref type
         * <p/>
         * <p/>
         * Value is a Pair&lt;Integer, Integer&gt;, where the key is the index of the class, the value the index of the
         * Name And Type.
         */
        FIELD(9) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                Pair<Integer, Integer> pair = (Pair<Integer, Integer>) value;
                stream.writeShort((int) pair.getKey().shortValue());
                stream.writeShort((int) pair.getValue().shortValue());
            }
        },
        /**
         * Methodref type
         * <p/>
         * <p/>
         * Value is a Pair&lt;Integer, Integer&gt;, where the key is the index of the class, the value the index of the
         * Name And Type.
         */
        METHOD(10) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                Pair<Integer, Integer> pair = (Pair<Integer, Integer>) value;
                stream.writeShort((int) pair.getKey().shortValue());
                stream.writeShort((int) pair.getValue().shortValue());
            }
        },
        /**
         * Interface Methodref type
         * <p/>
         * <p/>
         * Value is a Pair&lt;Integer, Integer&gt;, where the key is the index of the class, the value the index of the
         * Name And Type.
         */
        INTERFACE_METHOD(11) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                Pair<Integer, Integer> pair = (Pair<Integer, Integer>) value;
                stream.writeShort((int) pair.getKey().shortValue());
                stream.writeShort((int) pair.getValue().shortValue());
            }
        },
        /**
         * Name and type info
         * <p/>
         * <p/>
         * Value is a Pair&lt;Integer, Integer&gt;, where the key is the index of the member name, the value the index
         * of the descriptor.
         */
        NAME_AND_TYPE(12) {
            @Override public void write(DataOutputStream stream, Object value) throws IOException {
                Pair<Integer, Integer> pair = (Pair<Integer, Integer>) value;
                stream.writeShort((int) pair.getKey().shortValue());
                stream.writeShort((int) pair.getValue().shortValue());
            }
        };

        /** The bytecode ID number for the tag */
        @Getter int tag;

        /**
         * A new bytecode structure with the tag ID
         *
         * @param tag the ID number of the structure
         */
        Type(int tag) {
            this.tag = tag;
        }

        /**
         * Writes the data as a byte array
         *
         * @param value the value to write. Check each tag, as the value may vary
         * @return the bytes of the written data
         */
        public byte[] writeData(Object value) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                @Cleanup DataOutputStream outputStream = new DataOutputStream(stream);

                outputStream.writeByte(this.tag);
                this.write(outputStream, value);

                return stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new byte[0];
        }

        /**
         * Writes data from a type
         *
         * @param stream the stream to write to
         * @param value  the value to write. Check each tag, as the value may vary
         * @throws IOException thrown if the value could not be written to the stream
         */
        abstract void write(DataOutputStream stream, Object value) throws IOException;
    }
}

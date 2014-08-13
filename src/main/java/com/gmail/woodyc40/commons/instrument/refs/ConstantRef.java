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

import lombok.Getter;
import lombok.Setter;

/**
 * The reference to a constant pool entry
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@Getter @Setter
public class ConstantRef {
    /** The index in the constant pool */
    private final   int              index;
    /** Type */
    private         ConstantRef.Type type;
    /** Value (depends on the type, see the Type for details on what the value should be */
    @Setter private Object           value;
    private         Class<?>         constInfo;

    /**
     * Builds the ConstantRef for a SharedSecrets constant pool entry reference
     *
     * @param type  the type of the reference
     * @param value the value to write. Check each tag, as the value may vary
     * @param index the index of the constant pool entry
     */
    public ConstantRef(ConstantRef.Type type, Object value, int index) {
        this.type = type;
        this.value = value;
        this.index = index;
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
        PADDING(0),
        /** String type in UTF8 */
        UTF8(1),

        /** Integer type */
        INTEGER(3),

        /** Float type */
        FLOAT(4),

        /** Long type */
        LONG(5),

        /** Double type */
        DOUBLE(6),
        /**
         * Class type
         * <p>
         * <p>value is the index of the class name, in short</p>
         */
        CLASS(7),
        /**
         * String type
         * <p>
         * <p>value is the index of the string, in short</p>
         */
        STRING(8),
        /**
         * Fieldref type
         * <p>
         * <p>Value is a Pair&lt;Integer, Integer&gt;, where the key is the index of the class, the value the index of
         * the Name And Type.</p>
         */
        FIELD(9),
        /**
         * Methodref type
         * <p>
         * <p>Value is a Pair&lt;Integer, Integer&gt;, where the key is the index of the class, the value the index of
         * the Name And Type.</p>
         */
        METHOD(10),
        /**
         * Interface Methodref type
         * <p>
         * <p>Value is a Pair&lt;Integer, Integer&gt;, where the key is the index of the class, the value the index of
         * the Name And Type.</p>
         */
        INTERFACE_METHOD(11),
        /**
         * Name and type info
         * <p>
         * <p>Value is a Pair&lt;Integer, Integer&gt;, where the key is the index of the member name, the value the
         * index of the descriptor.</p>
         */
        NAME_AND_TYPE(12),

        /**
         * Member reference information. Cannot be used.
         */
        MEMBER_REF(-1);

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
    }
}

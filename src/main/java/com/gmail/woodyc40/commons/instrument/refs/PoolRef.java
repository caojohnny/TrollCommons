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

import com.gmail.woodyc40.commons.reflection.chain.ReflectionChain;
import javassist.bytecode.ConstPool;

/**
 * The reference to the entire constant pool itself
 *
 * @author AgentTroll
 * @version 1.0
 */
public class PoolRef {
    /** The reference to the original constat references */
    private final LongVectorRef longVectorRef;

    /** The javassist LongVector */
    private final Object longVector;

    /**
     * Builds a new constant pool reference from the javassist constant pool
     *
     * @param pool the javassist constant pool
     */
    public PoolRef(ConstPool pool) {
        this.longVector =
                new ReflectionChain(ConstPool.class).field().field("items").instance(pool).getter().get().reflect();
        this.longVectorRef = new LongVectorRef(this.longVector);
    }

    /**
     * Adds an entry to the constant pool
     *
     * @param ref the reference to the entry to add
     */
    public void addRef(ConstantRef ref) {
        this.longVectorRef.addElement(ref);
    }

    /**
     * Complete adding the values to the constant pool and push changes to the javassist long vector
     */
    public void finish() {
        this.longVectorRef.finish();
    }
}

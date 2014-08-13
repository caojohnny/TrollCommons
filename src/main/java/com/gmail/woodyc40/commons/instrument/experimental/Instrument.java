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

package com.gmail.woodyc40.commons.instrument.experimental;

import com.gmail.woodyc40.commons.instrument.CpTransformer;
import com.gmail.woodyc40.commons.instrument.refs.ConstantRef;
import com.gmail.woodyc40.commons.instrument.refs.PoolRef;
import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import sun.misc.SharedSecrets;

import java.util.HashSet;
import java.util.Set;

/**
 * Base accessor for experimental assembly instrumentation
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public class Instrument implements com.gmail.woodyc40.commons.instrument.Instrument {
    /** The class to instrument */
    private final Class<?>     base;
    /** The transformers for the constant pool */
    private final Set<CpTransformer> transformers = new HashSet<>(); // Work on this ! TODO
    /** Reference to the constant pool wrapper */
    private final PoolRef poolRef;

    /**
     * Builds a new instrumentation handler
     *
     * @param base the class to instrument
     */
    public Instrument(Class<?> base) {
        this.base = base;
        this.poolRef = new PoolRef(base, SharedSecrets.getJavaLangAccess().getConstantPool(this.base));
    }

    @Override public void acceptTransformer(CpTransformer transformer) {
        this.transformers.add(transformer);
    }

    @Override public PoolRef getConstantPool() {
        return null; // TODO
    }

    @Override public void finish() {
        for (int i = 0; i < this.poolRef.getSize(); i++) {
            for (CpTransformer trans : this.transformers) {
                ConstantRef ref = this.poolRef.read(i);
                Object before = ref.getValue();

                trans.transform(this, ref);
                UnsafeProvider.getProvider().putObject(before, 8L, ref.getValue());
            }
        }
    }
}

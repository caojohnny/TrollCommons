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

package com.gmail.woodyc40.commons.instrument;

import com.gmail.woodyc40.commons.instrument.refs.ConstantRef;
import com.gmail.woodyc40.commons.instrument.refs.PoolRef;

/**
 * Transformer access for entries in the constant pool
 *
 * @author AgentTroll
 * @version 1.0
 */
public interface CpTransformer {
    /**
     * Allows the constant pool entry to be modified
     *
     * @param ref the current reference of the constant the iteration is on - this will be called for each entry in the
     *            constant pool
     * @return the transformed constant, or the same constant if not changed. {@code null} to remove.
     */
    ConstantRef transform(ConstantRef ref);

    /**
     * Allows the constant pool as a whole to be modified
     *
     * @param ref the reference to the constant pool
     * @return the transformed constant pool
     */
    PoolRef transform(PoolRef ref);
}

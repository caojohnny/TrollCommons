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

package com.gmail.woodyc40.commons.collect;

public class HashingCache<K, V> implements Cache<K, V> {
    /** The delegate */
    private final HashingCache<K, V>.Struct struct =
            new HashingCache<K, V>.Struct();

    @Override public V lookup(K k) {
        return this.struct.get(k);
    }

    @Override public V insert(K k, V v) {
        this.struct.put(k, v);
        return v;
    }

    /**
     * Internal implementation of a hashing struct using JAVA hash strategy for cache storage
     *
     * @author AgentTroll
     * @version 1.0
     * @see com.gmail.woodyc40.commons.collect.AbstractHashStruct
     */
    private class Struct extends AbstractHashStruct<K, V> {
        {
            this.setStategy(AbstractHashStruct.HashStrategy.JAVA);
        }

        @Override protected AbstractHashStruct.Node[] buckets() {
            return new AbstractHashStruct.Node[16];
        }
    }
}

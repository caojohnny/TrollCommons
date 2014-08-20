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

package com.gmail.woodyc40.commons;

import com.gmail.woodyc40.commons.collect.AbstractHashStruct;

public final class BashMap {
    private static final AbstractHashStruct hashStruct = new AbstractHashStruct() {
        @Override protected AbstractHashStruct.Node[] buckets() {
            return new AbstractHashStruct.Node[16];
        }
    };

    private BashMap() {}

    public static void main(String... args) {
        while (true) {
            BashMap.hashStruct.get(new Object());
        }
    }
}
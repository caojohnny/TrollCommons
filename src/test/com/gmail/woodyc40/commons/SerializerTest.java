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

import com.gmail.woodyc40.commons.io.Serializer;

public final class SerializerTest {
    private SerializerTest() {}

    public static void main(String... args) {
        Object object = new Object();
        Serializer<Object> serializer = new Serializer<>(object);
        System.out.println(object.hashCode());
        byte[] bytes = serializer.serialize();
        System.out.println(Serializer.deserialize(bytes).hashCode());
    }
}

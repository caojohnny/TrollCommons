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

package com.gmail.woodyc40.commons.io;

import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Serialization utility for fast conversion of an Object to bytes, and back again.
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.1
 */
@RequiredArgsConstructor
public class Serializer<T> {
    private static final long OBJ_OFF = (long) UnsafeProvider.getProvider().arrayBaseOffset(Object[].class);

    private final T object;

    public static Object deserialize(byte[] bytes) {
        Serializer.Anchor anchor = new Serializer.Anchor();
        UnsafeProvider.getProvider().copyMemory(bytes,
                                                0L,
                                                anchor.getAnchor(),
                                                Serializer.getAddress(anchor.getAnchor()),
                                                (long) bytes.length);

        return anchor.getAnchor();
    }

    private static long getAddress(Object object) {
        Object[] array = { object };
        long baseOffset = (long) UnsafeProvider.getProvider().arrayBaseOffset(Object[].class);
        return UnsafeProvider.normalize(UnsafeProvider.getProvider().getLong(array, baseOffset));
    }

    public byte[] serialize() {
        long size = UnsafeProvider.sizeOf(this.object);
        byte[] bytes = new byte[(int) size];

        UnsafeProvider.getProvider().copyMemory(this.object, 0L, bytes, Serializer.getAddress(bytes), size);

        return bytes;
    }

    private static class Anchor {
        private static final  long   ANCHOR_OFF = Serializer.Anchor.getAnchorOff();
        @Getter private final Object anchor     = new Object();

        private static long getAnchorOff() {
            try {
                return UnsafeProvider.getProvider().objectFieldOffset(
                        Serializer.Anchor.class.getDeclaredField("anchor"));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            return 0L;
        }
    }
}

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

package com.gmail.woodyc40.commons.nmsobc.protocol;

import com.gmail.woodyc40.commons.event.*;
import lombok.*;
import net.minecraft.server.v1_7_R4.Packet;

/**
 * Represents a packet that was sent or recieved by the server
 *
 * @author AgentTroll
 * @version 1.0
 */
@Getter @RequiredArgsConstructor @Handler(EventType.PACKET)
public class PacketEvent implements CustomEvent {
    /** The sent or recieved packet */
    @Getter private final            Packet            packet;
    /** The intended direction of the packet */
    @Getter private final            PacketEvent.Bound bound;
    /** Whether or not this event is cancelled */
    @Setter private volatile boolean           cancelled;

    @Override public boolean getCancelled() {
        return this.cancelled;
    }

    /**
     * Represents the direction the packet is going toawads
     *
     * @author AgentTroll
     * @version 1.0
     */
    public enum Bound {
        /** Means the packet is sent to the client */
        CLIENT_BOUND,

        /** Means the packet is sent to the server */
        SERVER_BOUND
    }
}

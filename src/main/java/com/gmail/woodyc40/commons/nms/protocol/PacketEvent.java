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

package com.gmail.woodyc40.commons.nms.protocol;

import com.gmail.woodyc40.commons.event.*;
import lombok.*;
import net.minecraft.server.v1_7_R4.Packet;
import org.bukkit.entity.Player;

@Getter @RequiredArgsConstructor @Handler(EventType.PACKET)
public class PacketEvent implements CustomEvent {
    private final            Packet            packet;
    private final            Player            player;
    private final            PacketEvent.Bound bound;
    @Setter private volatile boolean           cancelled;

    @Override public boolean getCancelled() {
        return this.cancelled;
    }

    public enum Bound {
        CLIENT_BOUND, SERVER_BOUND;
    }
}

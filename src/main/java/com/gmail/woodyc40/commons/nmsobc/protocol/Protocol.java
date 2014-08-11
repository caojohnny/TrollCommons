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

import org.bukkit.plugin.Plugin;

/**
 * The main accessor to the {@link com.gmail.woodyc40.commons.nmsobc.protocol.ProtocolHandler} package
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public final class Protocol {
    /** The handler that manages the protocol */
    private static ProtocolHandler protocol;

    private Protocol() {}

    /**
     * Starts the protocol handling managers
     *
     * @param plugin the plugin to intiate with
     */
    public static void initiate(Plugin plugin) {
        Protocol.protocol = new ProtocolHandler(plugin);
    }

    /**
     * Creates a new packet builder
     *
     * @return the packet builder
     */
    public static PacketCreator createPacket() {
        return new PacketCreator();
    }

    /**
     * Gets the protocl manager
     *
     * @return the protocol manager, if initialized.
     */
    public static ProtocolHandler getProtocol() {
        return Protocol.protocol;
    }
}

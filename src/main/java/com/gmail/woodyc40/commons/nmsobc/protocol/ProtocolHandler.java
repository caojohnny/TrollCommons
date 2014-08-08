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

import com.gmail.woodyc40.commons.event.CustomEvent;
import com.gmail.woodyc40.commons.event.Events;
import com.gmail.woodyc40.commons.nmsobc.McServer;
import com.gmail.woodyc40.commons.reflection.FieldManager;
import com.gmail.woodyc40.commons.reflection.chain.ReflectionChain;
import lombok.Getter;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.io.netty.channel.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * The packet handler that will listen for packet writes and reads, as well as listen for player join and leave to cache
 * their connection to the server on behalf of the protocol package.
 *
 * @author AgentTroll
 * @version 1.0
 */
public final class ProtocolHandler {
    /** The connection cache that holds netty channels */
    @Getter private static final Map<Player, Channel> cache = new HashMap<>();
    private static final FieldManager<MinecraftServer, ServerConnection> serverChannelHandler =
            (FieldManager<MinecraftServer, ServerConnection>) new ReflectionChain(McServer.getMcServer()
                                                                                          .getClass()).field()
                                                                                                      .fieldFuzzy(
                                                                                                              McServer.getClass(
                                                                                                                      "ServerConnection"),
                                                                                                              0)
                                                                                                      .getManager();
    /** The plugin that is activated by this handler. Will be {@code null} when disabled */
    private volatile Plugin plugin;

    /**
     * Inits this handler. Registers internal listeners. Sets the plugin flag.
     *
     * @param plugin the plugin to use in this handler.
     */
    public ProtocolHandler(Plugin plugin) {
        Bukkit.getServer().getPluginManager()
              .registerEvents(new ProtocolListener(), plugin);
        this.plugin = plugin;
    }

    public void putProxy(Channel channel) {
        channel.pipeline().addBefore("packet_handler", "ProtocolHandler - BukkitCommons", new PacketAdapter());
    }

    // Private classes are shielded from access.
    // They are handled correctly internally.
    // If not really "inaccessible",
    // At least very difficult to get to,
    // Slow (or fast) reflect operations must be performed
    // On classlevel native search using forName(String)

    /**
     * Represents the handler inserted first into the minecraft protocol manager to handle incoming and outgoing
     * connection packets, and fire the event in the event framework.
     *
     * @author AgentTroll
     * @version 1.0
     */
    private class PacketAdapter extends ChannelDuplexHandler {
        /**
         * Handles incoming packets
         *
         * @param ctx the context which the packet is received in
         * @param msg the packet itself
         * @throws Exception if an error occurs
         */
        @Override public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Packet packet = (Packet) msg;
            CustomEvent event = new PacketEvent(packet, PacketEvent.Bound.SERVER_BOUND);

            Events.call(event);
            System.out.println("Incoming packet");
            if (event.getCancelled()) return;

            super.channelRead(ctx, msg);
        }

        /**
         * Handles outgoing packets
         *
         * @param ctx     the context which the packet is sent in
         * @param msg     the packet itself
         * @param promise the promise made to the channel
         * @throws Exception if an error occurs
         */
        @Override public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Packet packet = (Packet) msg;
            CustomEvent event = new PacketEvent(packet, PacketEvent.Bound.CLIENT_BOUND);

            Events.call(event);
            System.out.println("Outgoing packet");
            if (event.getCancelled()) return;

            super.write(ctx, msg, promise);
        }

        /**
         * Handles exceptions occurring in the Channel
         *
         * @param ctx   the context which the exception occurs in
         * @param cause the cause of the exception
         * @throws Exception if yet another exception occurs
         */
        @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }

    /**
     * The internal listener that adds the handlers to the player's connection and cleans up after the handler
     *
     * @author AgentTroll
     * @version 1.0
     */
    private class ProtocolListener implements Listener {
        /** Represents the channel ("m" as of 1.7.10 snapshot) field in NetworkManager */
        private final FieldManager<NetworkManager, Channel> CHANNEL =
                (FieldManager<NetworkManager, Channel>) new ReflectionChain(NetworkManager.class)
                        .field().fieldFuzzy(Channel.class, 0).getManager();

        /**
         * When a player joins
         *
         * @param event the event called
         */
        @EventHandler public void join(PlayerJoinEvent event) {
            if (ProtocolHandler.this.plugin == null) return; // Whoopsie, plugin disabled.

            EntityPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
            Channel conn = this.CHANNEL.get(player.playerConnection.networkManager);

            conn.pipeline().addLast("PacketListener" + player.getName(),
                                    new PacketAdapter());
            ProtocolHandler.getCache().put(event.getPlayer(), conn);
        }

        /**
         * When a player leaves
         *
         * @param event the event called
         */
        @EventHandler public void leave(PlayerQuitEvent event) {
            this.extract(event.getPlayer());
        } // In case some idiot leaves his Player objects lying around

        /**
         * When a player is kicked
         *
         * @param event the event called
         */
        @EventHandler public void kick(PlayerKickEvent event) {
            this.extract(event.getPlayer());
        } // Same with this

        /**
         * When the plugin is disabled
         *
         * @param event the event called
         */
        @EventHandler public void disable(PluginDisableEvent event) {
            if (event.getPlugin().equals(ProtocolHandler.this.plugin)) {
                ProtocolHandler.this.plugin = null; // Marker
                for (Player player : Bukkit.getServer().getOnlinePlayers())
                    this.extract(player);
            }
        }

        /**
         * Removes the handler from the player
         *
         * @param player the player to remove the handler from
         */
        public void extract(Player player) {
            Channel conn = ProtocolHandler.getCache().get(player);
            if (conn == null) return;

            String handler = "PacketListener" + player.getName();
            if (conn.pipeline().names().contains(handler))
                conn.pipeline().remove(handler);
            ProtocolHandler.getCache().remove(player);
        }
    }
}

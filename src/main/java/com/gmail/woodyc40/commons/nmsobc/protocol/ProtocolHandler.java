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
import com.gmail.woodyc40.commons.nmsobc.CbServer;
import com.gmail.woodyc40.commons.nmsobc.McServer;
import com.gmail.woodyc40.commons.reflection.FieldManager;
import com.gmail.woodyc40.commons.reflection.chain.ReflectionChain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.io.netty.channel.*;
import org.bukkit.Bukkit;
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
 * @since 1.0
 */
public final class ProtocolHandler { // TODO server channels
    /** The connection cache that holds netty channels */
    @Getter private static final Map<Player, Channel> cache = new HashMap<>();
    /** The name of the handlers */
    private final                String               name  = "ProtocolHandler - BukkitCommons";
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

    public void putProxy(Channel channel, Player player) {
        channel.pipeline().addLast(this.name, new ProtocolHandler.PacketAdapter(player));
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
     * @since 1.0
     */
    @RequiredArgsConstructor
    private class PacketAdapter extends ChannelDuplexHandler {
        /** The player that this intercepts. Can be null for server channels */
        private final Player player;

        /**
         * Handles incoming packets
         *
         * @param ctx the context which the packet is received in
         * @param msg the packet itself
         * @throws Exception if an error occurs
         */
        @Override public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            CustomEvent event = new PacketEvent(msg, this.player, PacketEvent.Bound.SERVER_BOUND);

            Events.call(event);
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
            CustomEvent event = new PacketEvent(msg, this.player, PacketEvent.Bound.CLIENT_BOUND);

            Events.call(event);
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
     * @since 1.0
     */
    private class ProtocolListener implements Listener {
        /** Represents the channel ("m" as of 1.7.10 snapshot) field in NetworkManager */
        private final FieldManager<Object, Channel> CHANNEL =
                (FieldManager<Object, Channel>) new ReflectionChain(McServer.getClass("NetworkManager"))
                        .field().fieldFuzzy(Channel.class, 0).getManager();

        /**
         * When a player joins
         *
         * @param event the event called
         */
        @EventHandler public void join(PlayerJoinEvent event) {
            if (ProtocolHandler.this.plugin == null) return; // Whoopsie, plugin disabled.

            Channel conn = this.CHANNEL.get(
                    new ReflectionChain(CbServer.cPlayerClass())
                            .method().method("getHandle").param(event.getPlayer()).invoker().invoke()      // 0
                            .field(McServer.getEPlayer()).field("playerConnection").last(0).getter().get() // 1
                            .field().field("networkManager").getter().get()                                // 2
                            .reflect());

            ProtocolHandler.this.putProxy(conn, event.getPlayer());
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
            final Channel conn = ProtocolHandler.getCache().get(player);
            if (conn == null) return;

            conn.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    if (conn.pipeline().names().contains(ProtocolHandler.this.name))
                        conn.pipeline().remove(ProtocolHandler.this.name);
                }
            });
            ProtocolHandler.getCache().remove(player);
        }
    }
}

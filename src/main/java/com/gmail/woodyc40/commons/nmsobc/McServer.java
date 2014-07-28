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

package com.gmail.woodyc40.commons.nmsobc;

import com.google.common.reflect.ClassPath;
import lombok.Getter;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents access to the NMS package without on the plugin side
 *
 * @author AgentTroll
 * @version 1.0
 */
public class McServer {
    /** All classes from the NMS package */
    @Getter private static final Map<String, Class<?>> classMap = new HashMap<>();
    /** The version represented in the format v#_#_R# taken from the {@link org.bukkit.Server} package */
    @Getter private final        String                version  =
            Bukkit.getServer().getClass().getPackage().getName().substring(23);

    // Map classes from NMS
    static {
        try {
            ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
            for (ClassPath.ClassInfo cls :
                    classPath.getTopLevelClasses("net.minecraft.server." + new McServer().getVersion())) {
                McServer.classMap.put(cls.getSimpleName(), cls.load());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Acquires a cached {@link java.lang.Class} from the net.minecraft.server package
     *
     * @param name the SIMPLE name of the class, without qualifiers
     * @return the class if found, {@code null} if not
     */
    public Class<?> getClass(String name) {
        return McServer.classMap.get(name);
    }

    /**
     * Gets the instance of net.minecraft.server.MinecraftServer
     *
     * <p>
     * There are a lot of entry points and accessors in MinecraftServer, hence the inclusion in this class
     *
     * @return the instance of MinecraftServer
     */
    public MinecraftServer getMcServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }
}

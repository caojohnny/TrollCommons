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

import com.gmail.woodyc40.commons.concurrent.JavaFork;
import com.gmail.woodyc40.commons.concurrent.ThreadPoolManager;
import com.gmail.woodyc40.commons.io.Files;
import com.gmail.woodyc40.commons.nmsobc.protocol.Protocol;
import com.gmail.woodyc40.commons.reflection.impl.ReflectionCache;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * {@link org.bukkit.plugin.java.JavaPlugin} {@code class} representing this plugin utility
 *
 * @author AgentTroll
 * @version 1.0
 */
public class Commons extends JavaPlugin {
    /** The plugin instance */
    @Getter private static Plugin plugin;

    /**
     * Gets the calling package for the method
     *
     * @param ignore {@code true} to prevent checks on BukkitCommons
     * @return the package the called the method
     */
    public static Package getCaller(boolean ignore) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            if (!element.getClassName().startsWith("java.") &&
                (!ignore || !element.getClassName().startsWith("com.gmail.woodyc40.commons.")))
                return ReflectionCache.getClass(element.getClassName()).getPackage();
        }

        return null;
    }

    @Override public void onEnable() {
        Commons.plugin = this;

        new Protocol().initiate(this);
        try {
            Files.update();
            JavaFork.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void onDisable() {
        new ThreadPoolManager().shutdown();
        JavaFork.shutdown();
    }
}

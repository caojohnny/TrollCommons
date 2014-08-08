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
import com.gmail.woodyc40.commons.instrument.experimental.Instrument;
import com.gmail.woodyc40.commons.io.Files;
import com.gmail.woodyc40.commons.nmsobc.protocol.Protocol;
import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.gmail.woodyc40.commons.reflection.impl.ReflectAccess;
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
    /** Classes that may be disabled due to availability */
    private static final Class<?>[] CLASSES        = {
            Instrument.class, UnsafeProvider.class, ReflectAccess.class,
            ReflectionCache.getClass("com.gmail.woodyc40.commons.reflection.impl.ConstructorImpl"),
            ReflectionCache.getClass("com.gmail.woodyc40.commons.reflection.impl.MethodImpl"),
            ReflectionCache.getClass("com.gmail.woodyc40.commons.reflection.impl.FieldImpl")
    };
    /** Classes that need to be available for specific libraries */
    private static final Class<?>[] UNSAFE_CLASSES = {
            ReflectionCache.getClass("sun.misc.Unsafe"),
            ReflectionCache.getClass("sun.reflect.MethodAccessor"),
            ReflectionCache.getClass("sun.reflect.ConstantPool"),
            ReflectionCache.getClass("sun.reflect.ConstructorAccessor"),
            ReflectionCache.getClass("sun.reflect.ReflectionFactory"),
            ReflectionCache.getClass("sun.misc.SharedSecrets")
    };
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

        for (Class<?> c : Commons.UNSAFE_CLASSES) {
            if (c == null) {}
                // TODO disable CLASSES
        }

        Protocol.initiate(this);
        try {
            Files.update();
            // JavaFork.start(); TODO
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    @Override public void onDisable() {
        ThreadPoolManager.shutdown();
        JavaFork.shutdown();
    }
}

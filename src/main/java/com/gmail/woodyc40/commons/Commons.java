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

import com.gmail.woodyc40.commons.concurrent.ThreadPoolManager;
import com.gmail.woodyc40.commons.event.Events;
import com.gmail.woodyc40.commons.io.Files;
import com.gmail.woodyc40.commons.reflection.impl.ReflectionCache;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * The main runner representing this plugin utility
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public class Commons {
    public static Package getCaller(boolean ignore) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            if (!element.getClassName().startsWith("java.") &&
                (!ignore || !element.getClassName().startsWith("com.gmail.woodyc40.commons."))) {
                Class<?> clazz = ReflectionCache.getClass(element.getClassName());
                if (clazz.isAssignableFrom(JavaPlugin.class) || clazz.getPackage().getName()
                                                                     .contains("com.gmail.woodyc40.commons"))
                    return clazz.getPackage();
            }
        }

        return null;
    }

    public void onEnable() {
        try {
            Files.update();
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    public void onDisable() {
        ThreadPoolManager.shutdown();
        Events.shutdown();
    }
}

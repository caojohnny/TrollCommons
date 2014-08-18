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

import com.gmail.woodyc40.commons.collect.AbstractHashStruct;
import com.gmail.woodyc40.commons.collect.StructBuilder;

import java.util.Map;

/**
 * The settings for each client given under the conditions set for the runtime instance
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public class Settings {
    /** The map of users */
    private static final Map<Package, Settings> SETTINGS_MAP =
            new StructBuilder().hash(AbstractHashStruct.HashStrategy.JAVA).buildMap();

    /** Whether or not to use non sun packages for reflection */
    private boolean safeReflection;

    /**
     * Gets the specific Settings instance stored for the calling plugin
     *
     * @return the Settings for the plugin that calls for a read or write to one of the settings
     */
    public static Settings forPackage() {
        Package pack = Commons.getCaller(false);
        Settings settings = Settings.SETTINGS_MAP.get(pack);
        if (settings == null) {
            settings = new Settings();
            Settings.SETTINGS_MAP.put(pack, settings);
        }

        return settings;
    }

    /**
     * Determines the usage of sun over langreflect
     *
     * @return the setting to use reflection libraries
     */
    public static boolean isSafeReflection() {
        return Settings.forPackage().safeReflection;
    }

    /**
     * Sets whether to use {@link sun.reflect} to perform reflection, or to use {@link java.lang.reflect} instead
     *
     * @param safe {@code true} to use {@link java.lang.reflect}, {@code false} to use {@link sun.reflect}
     */
    public static void setSafeReflection(boolean safe) {
        Settings.forPackage().safeReflection = safe;
    }
}

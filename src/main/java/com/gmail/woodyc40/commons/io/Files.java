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

package com.gmail.woodyc40.commons.io;

import com.gmail.woodyc40.commons.Commons;
import lombok.Cleanup;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The related directories to which this plugin is required to know
 *
 * @author AgentTroll
 * @version 1.0
 */
public class Files {
    /** Cached plugin directory */
    private static File plugins;
    /** Cached jar file */
    private static File jar;

    /**
     * Get the cached plugin directory
     *
     * @return the plugin directory
     */
    public static File pluginDir() {
        return Files.plugins;
    }

    /**
     * Get the cached jar file representing the compressed classes of this library
     *
     * @return the jar file
     */
    public static File jarFile() {
        return Files.jar;
    }

    /**
     * Updates the caches
     *
     * @throws IOException when the jar file cannot be accessed
     */
    public static void update() throws IOException {
        File dir = new File(Commons.class.getProtectionDomain()
                                         .getCodeSource()
                                         .getLocation()
                                         .getPath()
                                         .replaceAll("%20", " "));
        Files.plugins = new File(dir.getParentFile().getPath());

        for (File file : Files.plugins.listFiles()) {
            @Cleanup JarFile jarFile = new JarFile(file.getName());
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!"plugin.yml".equals(entry.getName()))
                    continue;

                // Deprecation does not make a difference in this case - it will still work regardless
                FileConfiguration config = YamlConfiguration.loadConfiguration(jarFile.getInputStream(entry));
                if (config.get("main").equals(Commons.class.getName()))
                    Files.jar = file;
            }
        }
    }
}

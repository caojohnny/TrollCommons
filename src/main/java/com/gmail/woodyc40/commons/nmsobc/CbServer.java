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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CbServer {
    /** All classes from the OBC package */
    @Getter private static final Map<String, Class<?>> classMap = new HashMap<>();

    // Map classes from OBC
    static {
        try {
            ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
            for (ClassPath.ClassInfo cls :
                    classPath.getTopLevelClasses("org.bukkit.craftbukkit." + new McServer().getVersion())) {
                CbServer.classMap.put(cls.getSimpleName(), cls.load());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Acquires a cached {@link java.lang.Class} from the org.bukkit.craftbukkit package
     *
     * @param name the SIMPLE name of the class, without qualifiers
     * @return the class if found, {@code null} if not
     */
    public Class<?> getClass(String name) {
        return CbServer.classMap.get(name);
    }

    public Class<?> cPlayerClass() { return this.getClass("CraftPlayer"); }

    public Class<?> cServerClass() { return this.getClass("CraftServer"); }
}

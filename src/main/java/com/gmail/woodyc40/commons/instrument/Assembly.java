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

package com.gmail.woodyc40.commons.instrument;

import java.io.InputStream;

/**
 * Utilities for instrumentation
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public final class Assembly {
    private Assembly() {} // No instantiation

    /**
     * Converts a class file to an input stream that represents its bytecode
     *
     * @param cls the class to convert
     * @return the inputstream of bytes
     */
    public static InputStream toStream(Class<?> cls) {
        String className = cls.getName();
        String classAsPath = className.replace('.', '/') + ".class";
        return cls.getClassLoader().getResourceAsStream(classAsPath);
    }
}

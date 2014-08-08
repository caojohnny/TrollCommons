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

package com.gmail.woodyc40.commons.instrument.asm;

import com.gmail.woodyc40.commons.concurrent.JavaFork;
import com.gmail.woodyc40.commons.instrument.Assembly;
import com.gmail.woodyc40.commons.instrument.CpTransformer;
import com.gmail.woodyc40.commons.instrument.refs.PoolRef;
import com.google.common.io.ByteStreams;
import javassist.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.*;
import java.security.ProtectionDomain;

/**
 * Base accessor for assembly modification using the ASM library
 *
 * @author AgentTroll
 * @version 1.0
 */
public class Instrument implements com.gmail.woodyc40.commons.instrument.Instrument {
    /** The ClassPool object from javassist - let's just cache it */
    public static final ClassPool CLASS_POOL = ClassPool.getDefault();

    /** The class that is being instrumented */
    @Getter(AccessLevel.PROTECTED) private final Class<?>    base;
    /** The byte stream for the class */
    @Getter(AccessLevel.PROTECTED) private final InputStream bytes;
    /** The javassist class wrapper */
    @Getter(AccessLevel.PROTECTED) private final CtClass     ctClass;
    /** The constant pool */
    @Getter private final                        PoolRef     constantPool;

    /**
     * Start modifying the top level member - the {@code class}
     *
     * @param base the starting {@code class}
     */
    public Instrument(Class<?> base) throws NotFoundException {
        this.base = base;
        this.bytes = Assembly.toStream(this.base);
        this.ctClass = Instrument.CLASS_POOL.getCtClass(base.getName());
        this.constantPool = new PoolRef(this.ctClass.getClassFile().getConstPool());
    }

    @Override public void acceptTransformer(CpTransformer transformer) {
        // ???
    }

    @Override public void finish() {
        ClassFileTransformer transformer = new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain
                    protectionDomain, byte[] bytes)
                    throws IllegalClassFormatException {
                try {
                    return Instrument.this.ctClass.toBytecode();
                } catch (IOException | CannotCompileException e) {
                    e.printStackTrace();
                }

                return new byte[0];
            }
        };

        Instrumentation instrumentation = JavaFork.getInstrument();
        instrumentation.addTransformer(transformer);

        try {
            instrumentation.redefineClasses(new ClassDefinition(this.base, ByteStreams.toByteArray(this.bytes)));
        } catch (ClassNotFoundException | UnmodifiableClassException | IOException e) {
            e.printStackTrace();
        }

        instrumentation.removeTransformer(transformer); // Clean up
    }
}

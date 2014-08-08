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

import com.gmail.woodyc40.commons.instrument.CpTransformer;
import com.gmail.woodyc40.commons.instrument.Instrument;
import com.gmail.woodyc40.commons.instrument.refs.ConstantRef;
import com.gmail.woodyc40.commons.instrument.refs.PoolRef;
import com.gmail.woodyc40.commons.reflection.ConstructorManager;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;
import com.gmail.woodyc40.commons.reflection.impl.ReflectAccess;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

public final class InstrumentTest {
    private static final ConstructorManager<Method> method =
            ReflectAccess.accessConstructor(ReflectionTool.forConstruct(Method.class,
                                                                        Class.class,
                                                                        String.class,
                                                                        Class[].class,
                                                                        Class.class,
                                                                        Class[].class,
                                                                        int.class,
                                                                        int.class,
                                                                        String.class,
                                                                        byte[].class,
                                                                        byte[].class,
                                                                        byte[].class));

    private InstrumentTest() {}

    public static void main(String... args) throws NoSuchMethodException {
        Instrument instrument = new com.gmail.woodyc40.commons.instrument.experimental.Instrument(InstrumentTest.class);
        instrument.acceptTransformer(new InstrumentTest.Transformer());
        instrument.finish();

        Class<?> returnType = InstrumentTest.class.getDeclaredMethod("getOnline").getReturnType();
        System.out.println(returnType.getName());
    }

    public static Object getOnline() {
        return Collections.emptySet();
    }

    private static class Transformer implements CpTransformer {
        private PoolRef ref;
        private int     transformIndex;

        @Override
        public ConstantRef transform(com.gmail.woodyc40.commons.instrument.experimental.Instrument instrument,
                                     ConstantRef ref) {
            if (ref.getType() != ConstantRef.Type.METHOD)
                return ref;
            Method method1 = (Method) ref.getValue();
            if (!"getOnline".equalsIgnoreCase(method1.getName()))
                return ref;
            ref.setValue(InstrumentTest.method.createInstance(method1.getDeclaringClass(),
                                                              method1.getName(),
                                                              method1.getParameterTypes(),
                                                              Collection.class,
                                                              method1.getExceptionTypes(),
                                                              method1.getModifiers(),
                                                              1,
                                                              "()Ljava/util/Collection;",
                                                              new byte[0],
                                                              new byte[0],
                                                              new byte[0]));
            return ref;
        }

        @Override public ConstantRef transform(ConstantRef ref) {
            return ref;
        }

        @Override public PoolRef transform(PoolRef ref) {
            this.ref = ref;
            return ref;
        }
    }
}

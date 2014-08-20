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

import com.gmail.woodyc40.commons.reflection.chain.ReflectionChain;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
Output:

# Run progress: 0.00% complete, ETA 00:01:20
# Warmup: 20 iterations, 1 s each
# Measurement: 20 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.ProxyTest.proxy
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 110648.653 ns/op
# Warmup Iteration   2: 20679.375 ns/op
# Warmup Iteration   3: 3936.388 ns/op
# Warmup Iteration   4: 4132.863 ns/op
# Warmup Iteration   5: 3440.347 ns/op
# Warmup Iteration   6: 3863.376 ns/op
# Warmup Iteration   7: 3242.136 ns/op
# Warmup Iteration   8: 3713.203 ns/op
# Warmup Iteration   9: 3075.057 ns/op
# Warmup Iteration  10: 3089.637 ns/op
# Warmup Iteration  11: 3248.121 ns/op
# Warmup Iteration  12: 2997.042 ns/op
# Warmup Iteration  13: 3198.538 ns/op
# Warmup Iteration  14: 2933.097 ns/op
# Warmup Iteration  15: 3174.051 ns/op
# Warmup Iteration  16: 3009.374 ns/op
# Warmup Iteration  17: 3365.965 ns/op
# Warmup Iteration  18: 2850.859 ns/op
# Warmup Iteration  19: 2978.739 ns/op
# Warmup Iteration  20: 3080.304 ns/op
Iteration   1: 3253.654 ns/op
Iteration   2: 2849.816 ns/op
Iteration   3: 3001.184 ns/op
Iteration   4: 2907.092 ns/op
Iteration   5: 3154.357 ns/op
Iteration   6: 2839.017 ns/op
Iteration   7: 3033.112 ns/op
Iteration   8: 3126.797 ns/op
Iteration   9: 2799.763 ns/op
Iteration  10: 2930.188 ns/op
Iteration  11: 2867.551 ns/op
Iteration  12: 2925.172 ns/op
Iteration  13: 2752.858 ns/op
Iteration  14: 2845.958 ns/op
Iteration  15: 2901.328 ns/op
Iteration  16: 2836.870 ns/op
Iteration  17: 3081.830 ns/op
Iteration  18: 3211.137 ns/op
Iteration  19: 2840.829 ns/op
Iteration  20: 2965.784 ns/op

Result: 2956.215 ±(99.9%) 124.593 ns/op [Average]
  Statistics: (min, avg, max) = (2752.858, 2956.215, 3253.654), stdev = 143.481
  Confidence interval (99.9%): [2831.622, 3080.808]


# Run progress: 50.00% complete, ETA 00:00:50
# Warmup: 20 iterations, 1 s each
# Measurement: 20 iterations, 1 s each
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.gmail.woodyc40.commons.ProxyTest.proxy0
# VM invoker: /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
# VM options: -Didea.launcher.port=7535 -Didea.launcher.bin.path=/media/A4F1-7AB7/linux/IntelliJ IDEA/bin -Dfile
.encoding=UTF-8
# Fork: 1 of 1
# Warmup Iteration   1: 6807.585 ns/op
# Warmup Iteration   2: 2292.427 ns/op
# Warmup Iteration   3: 1989.160 ns/op
# Warmup Iteration   4: 1979.639 ns/op
# Warmup Iteration   5: 2129.825 ns/op
# Warmup Iteration   6: 1969.237 ns/op
# Warmup Iteration   7: 2050.130 ns/op
# Warmup Iteration   8: 1748.542 ns/op
# Warmup Iteration   9: 1832.324 ns/op
# Warmup Iteration  10: 1795.307 ns/op
# Warmup Iteration  11: 1887.331 ns/op
# Warmup Iteration  12: 1738.311 ns/op
# Warmup Iteration  13: 1828.610 ns/op
# Warmup Iteration  14: 1753.556 ns/op
# Warmup Iteration  15: 1780.476 ns/op
# Warmup Iteration  16: 1772.864 ns/op
# Warmup Iteration  17: 1815.042 ns/op
# Warmup Iteration  18: 1755.947 ns/op
# Warmup Iteration  19: 1712.652 ns/op
# Warmup Iteration  20: 1710.547 ns/op
Iteration   1: 1666.563 ns/op
Iteration   2: 1697.078 ns/op
Iteration   3: 1670.897 ns/op
Iteration   4: 1691.851 ns/op
Iteration   5: 1782.780 ns/op
Iteration   6: 1723.458 ns/op
Iteration   7: 1771.169 ns/op
Iteration   8: 1703.087 ns/op
Iteration   9: 1713.236 ns/op
Iteration  10: 1704.708 ns/op
Iteration  11: 1727.206 ns/op
Iteration  12: 1689.021 ns/op
Iteration  13: 1671.933 ns/op
Iteration  14: 1758.227 ns/op
Iteration  15: 1743.968 ns/op
Iteration  16: 1759.284 ns/op
Iteration  17: 1812.998 ns/op
Iteration  18: 1873.521 ns/op
Iteration  19: 1712.522 ns/op
Iteration  20: 1686.957 ns/op

Result: 1728.023 ±(99.9%) 45.733 ns/op [Average]
  Statistics: (min, avg, max) = (1666.563, 1728.023, 1873.521), stdev = 52.666
  Confidence interval (99.9%): [1682.290, 1773.756]


# Run complete. Total time: 00:01:39

Benchmark                    Mode   Samples        Score  Score error    Units
c.g.w.c.ProxyTest.proxy      avgt        20     2956.215      124.593    ns/op
c.g.w.c.ProxyTest.proxy0     avgt        20     1728.023       45.733    ns/op
 */
public class ProxyTest {
    private static final Map<Object, Object> lol = new HashMap<>();

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + ProxyTest.class.getSimpleName() + ".*")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(20)
                .measurementIterations(20)
                .build();

        new Runner(opt).run();
    }

    @Benchmark @Fork(1) public static Object proxy() {
        ProxyTest.ProxyProxy proxy = ProxyTest.Trap.create(ProxyTest.ProxyProxy.class, new ProxyTest());
        return proxy.getLolMap();
    }

    @Benchmark @Fork(1) public static Object proxy0() {
        return new ReflectionChain(ProxyTest.class)
                .field().field("lol").instance(new ProxyTest())
                .getter().get().reflect();
    }

    public enum TrapTagType {
        METHOD, SETTER, GETTER
    }

    private interface ProxyProxy {
        @ProxyTest.TrapTag(type = ProxyTest.TrapTagType.GETTER, value = "lol") Map<Object, Object> getLolMap();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TrapTag {
        ProxyTest.TrapTagType type() default ProxyTest.TrapTagType.METHOD;

        String value() default "";
    }

    /* ClassTrap v1.0.1 by _MylesC (MylesIsCool)  */
    public static class Trap implements InvocationHandler {
        private static final Map<Class, Class> primitiveMap = new HashMap<>();

        static {
            ProxyTest.Trap.primitiveMap.put(Boolean.class, boolean.class);
            ProxyTest.Trap.primitiveMap.put(Character.class, char.class);
            ProxyTest.Trap.primitiveMap.put(Byte.class, byte.class);
            ProxyTest.Trap.primitiveMap.put(Short.class, short.class);
            ProxyTest.Trap.primitiveMap.put(Integer.class, int.class);
            ProxyTest.Trap.primitiveMap.put(Long.class, long.class);
            ProxyTest.Trap.primitiveMap.put(Float.class, float.class);
            ProxyTest.Trap.primitiveMap.put(Double.class, double.class);
            ProxyTest.Trap.primitiveMap.put(Void.class, void.class);
        }

        private final Object internal_object;

        private Trap(Object o) {
            this.internal_object = o;
        }

        public static <T> T create(Class<T> classInterface, Object o) {
            return (T) Proxy.newProxyInstance(classInterface.getClassLoader(),
                                              new Class[] { classInterface },
                                              new ProxyTest.Trap(o));
        }

        public static <T> T create(Class<T> classInterface, Class objectClass) {
            return ProxyTest.Trap.create(classInterface, objectClass, new Object[0]);
        }

        public static <T> T create(Class<T> classInterface, Class objectClass, Object... constructorParams) {
            return ProxyTest.Trap.create(classInterface,
                                         objectClass,
                                         ProxyTest.Trap.extractClasses(constructorParams),
                                         constructorParams);
        }

        public static <T> T create(Class<T> classInterface, Class objectClass, Class[] constructorClasses,
                                   Object... constructorParams) {
            return ProxyTest.Trap.create(classInterface,
                                         ProxyTest.Trap.createObject(objectClass,
                                                                     constructorClasses,
                                                                     constructorParams));
        }

        public static <T> T createObject(Class<T> objectClass) {
            return ProxyTest.Trap.createObject(objectClass, new Class[0], new Object[0]);
        }

        public static <T> T createObject(Class<T> objectClass, Object... constructorParams) {
            return ProxyTest.Trap.createObject(objectClass,
                                               ProxyTest.Trap.extractClasses(constructorParams),
                                               constructorParams);
        }

        public static <T> T createObject(Class<T> objectClass, Class[] constructorClasses,
                                         Object... constructorParams) {
            try {
                T object;
                if (objectClass.getConstructors().length == 0)
                    object = objectClass.getConstructor().newInstance();
                else {
                    Constructor constructor = null;
                    for (Constructor x : objectClass.getDeclaredConstructors()) {
                        if (x.getParameterTypes().length == constructorClasses.length) {
                            boolean works = true;
                            int count = 0;
                            for (Class c : x.getParameterTypes())
                                if (!c.isAssignableFrom(constructorClasses[count++].getClass()) &&
                                    !c.equals(constructorClasses[count - 1]) &&

                                    ProxyTest.Trap.isNotSamePrimitive(c, constructorClasses[count - 1]))
                                    works = false;
                            if (works)
                                constructor = x;
                        }
                    }
                    if (constructor == null)
                        throw new Exception("Failed to find a compatible constructor for the given arguments.");
                    object = (T) constructor.newInstance(constructorParams);
                }
                return object;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Class[] extractClasses(Object... objects) {
            List<Class> paramClasses = new ArrayList<>();
            for (Object o : objects)
                paramClasses.add(o.getClass());
            return paramClasses.toArray(new Class[paramClasses.size()]);
        }

        public static boolean isNotSamePrimitive(Class a, Class b) {
            return !a.equals(b) && !ProxyTest.Trap.toPrimitive(a).equals(ProxyTest.Trap.toPrimitive(b));
        }

        public static Class toPrimitive(Class b) {
            return ProxyTest.Trap.primitiveMap.containsKey(b) ? ProxyTest.Trap.primitiveMap.get(b) : b;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            ProxyTest.TrapTag t = method.getAnnotation(ProxyTest.TrapTag.class);
            Method desired = null;
            if (t == null) {
                try {
                    desired = this.internal_object.getClass()
                                                  .getDeclaredMethod(method.getName(), method.getParameterTypes());
                } catch (NoSuchMethodException e) {
                    throw new Exception("Could not find method '" + method.getName() +
                                        "' as inherited from method name, consider using a TrapTag?");
                }
            } else {
                String desiredValue = t.value();
                if (t.type() == ProxyTest.TrapTagType.METHOD) {
                    for (Method x : this.internal_object.getClass().getDeclaredMethods()) {
                        if (x.getParameterTypes().length == args.length) {
                            boolean works = true;
                            int count = 0;
                            for (Class c : x.getParameterTypes())
                                if (!c.isAssignableFrom(args[count++].getClass()) && !c.equals(args[count - 1]
                                                                                                       .getClass()) &&
                                    ProxyTest.Trap.isNotSamePrimitive(c, args[count - 1].getClass()))
                                    works = false;
                            if (works)
                                desired = x;
                        }
                    }
                    if (desired == null)
                        throw new Exception("Could not find method '" + desiredValue + "' as specified in TrapTag.");
                } else {
                    try {
                        Field desiredField = this.internal_object.getClass().getDeclaredField(t.value());
                        desiredField.setAccessible(true);
                        if (t.type() == ProxyTest.TrapTagType.GETTER)
                            if ("this".equals(t.value()))
                                return this.internal_object;
                            else
                                return desiredField.get(this.internal_object);
                        if (t.type() == ProxyTest.TrapTagType.SETTER) {
                            if (args.length == 0)
                                throw new Exception(
                                        "Specified setter for '" + desiredValue + "' but parameter 1 missing.");
                            desiredField.set(this.internal_object, args[0]);
                            return null;
                        }
                    } catch (NoSuchFieldException e) {
                        throw new Exception("Could not find field '" + desiredValue + "' as specified in TrapTag.");
                    }
                }
            }
            desired.setAccessible(true);
            Object result = desired.invoke(this.internal_object, args);
            if (method.getReturnType() != null && method.getReturnType().getAnnotation(ProxyTest.TrapTag.class) != null)
                return ProxyTest.Trap.create(method.getReturnType(), result);
            return result;
        }
    }
}

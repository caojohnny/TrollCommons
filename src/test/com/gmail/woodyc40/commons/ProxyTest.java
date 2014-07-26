package com.gmail.woodyc40.commons;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.*;
import java.util.*;

public class ProxyTest {
    private static final Map<Object, Object> lol = new HashMap<>();

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + HashBenchmark.class.getSimpleName() + ".*")
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }

    @Benchmark @Fork(1) public Object proxy() {
        ProxyProxy proxy = Trap.create(ProxyProxy.class, new ProxyTest());
        return proxy.getLolMap();
    }

    @Benchmark @Fork(1) public Object proxy0() {
        ProxyProxy proxy = Trap.create(ProxyProxy.class, new ProxyTest());
        return proxy.getLolMap();
    }

    public enum TrapTagType {
        METHOD, SETTER, GETTER
    }


    private interface ProxyProxy {
        @TrapTag(type = TrapTagType.GETTER, value = "lol")
        public Map<Object, Object> getLolMap();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TrapTag {
        TrapTagType type() default TrapTagType.METHOD;
        String value() default "";
    }

    /* ClassTrap v1.0.1 by _MylesC (MylesIsCool)  */
    public static class Trap implements InvocationHandler {
        private static final Map<Class, Class> primitiveMap = new HashMap<Class, Class>();
        static {
            primitiveMap.put(Boolean.class, boolean.class);
            primitiveMap.put(Character.class, char.class);
            primitiveMap.put(Byte.class, byte.class);
            primitiveMap.put(Short.class, short.class);
            primitiveMap.put(Integer.class, int.class);
            primitiveMap.put(Long.class, long.class);
            primitiveMap.put(Float.class, float.class);
            primitiveMap.put(Double.class, double.class);
            primitiveMap.put(Void.class, void.class);
        }
        private final Object internal_object;

        private Trap(Object o) {
            this.internal_object = o;
        }

        public static <T> T create(Class<T> classInterface, Object o) {
            return (T) Proxy.newProxyInstance(classInterface.getClassLoader(),
                                              new Class[] { classInterface },
                                              new Trap(o));
        }

        public static <T> T create(Class<T> classInterface, Class objectClass) {
            return create(classInterface, objectClass, new Object[0]);
        }

        public static <T> T create(Class<T> classInterface, Class objectClass, Object... constructorParams) {
            return create(classInterface, objectClass, extractClasses(constructorParams), constructorParams);
        }

        public static <T> T create(Class<T> classInterface, Class objectClass, Class[] constructorClasses, Object... constructorParams) {
            return create(classInterface, Trap.createObject(objectClass, constructorClasses, constructorParams));
        }

        public static <T> T createObject(Class<T> objectClass) {
            return createObject(objectClass, new Class[0], new Object[0]);
        }

        public static <T> T createObject(Class<T> objectClass, Object... constructorParams) {
            return createObject(objectClass, extractClasses(constructorParams), constructorParams);
        }

        public static <T> T createObject(Class<T> objectClass, Class[] constructorClasses, Object... constructorParams) {
            try {
                T object;
                if (objectClass.getConstructors().length == 0)
                    object = objectClass.newInstance();
                else {
                    Constructor constructor = null;
                    for (Constructor x : objectClass.getDeclaredConstructors()) {
                        if (x.getParameterTypes().length == constructorClasses.length) {
                            boolean works = true;
                            int count = 0;
                            for (Class c : x.getParameterTypes())
                                if (!c.isAssignableFrom(constructorClasses[count++].getClass()) && !c.equals(constructorClasses[count - 1]) && isNotSamePrimitive(c, constructorClasses[count - 1]))
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

        public static Class[] extractClasses(Object[] objects) {
            List<Class> paramClasses = new ArrayList<Class>();
            for (Object o : objects)
                paramClasses.add(o.getClass());
            return paramClasses.toArray(new Class[paramClasses.size()]);
        }

        public static boolean isNotSamePrimitive(Class a, Class b) {
            return !a.equals(b) && !toPrimitive(a).equals(toPrimitive(b));
        }

        public static Class toPrimitive(Class b) {
            return primitiveMap.containsKey(b) ? primitiveMap.get(b) : b;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            TrapTag t = method.getAnnotation(TrapTag.class);
            Method desired = null;
            if (t == null) {
                try {
                    desired = this.internal_object.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
                } catch (NoSuchMethodException e) {
                    throw new Exception("Could not find method '" + method.getName() + "' as inherited from method name, consider using a TrapTag?");
                }
            } else {
                String desiredValue = t.value();
                if (t.type() == TrapTagType.METHOD) {
                    for (Method x : this.internal_object.getClass().getDeclaredMethods()) {
                        if (x.getParameterTypes().length == args.length) {
                            boolean works = true;
                            int count = 0;
                            for (Class c : x.getParameterTypes())
                                if (!c.isAssignableFrom(args[count++].getClass()) && !c.equals(args[count - 1].getClass()) && isNotSamePrimitive(c, args[count - 1].getClass()))
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
                        if (t.type() == TrapTagType.GETTER)
                            if (t.value().equals("this"))
                                return this.internal_object;
                            else
                                return desiredField.get(this.internal_object);
                        if (t.type() == TrapTagType.SETTER) {
                            if (args.length == 0)
                                throw new Exception("Specified setter for '" + desiredValue + "' but parameter 1 missing.");
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
            if (method.getReturnType() != null && method.getReturnType().getAnnotation(TrapTag.class) != null)
                return create(method.getReturnType(), result);
            return result;
        }
    }
}

package com.gmail.woodyc40.commons.reflection.chain;

import com.gmail.woodyc40.commons.reflection.MethodManager;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;
import com.gmail.woodyc40.commons.reflection.impl.ReflectAccess;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.*;

@RequiredArgsConstructor
public class MethodLinkImpl implements MethodLink {
    private final Class<Object> holder;
    private final ReflectionChain parent;
    private List<Object> params;
    private MethodManager<Object, Object> method;

    @Override public void last(int index) {
        this.params.add(this.parent.returned.get(index));
    }

    @Override public void param(Object... obj) {
        this.params.addAll(Arrays.asList(obj));
    }

    @Override public Invoker invoker() {
        return new InvokerImpl();
    }

    @Override public MethodLink invoke(String name, Class[] args) {
        this.method = ReflectAccess.accessMethod(ReflectionTool.forMethod(name, this.holder, args));
        return this;
    }

    @Override public MethodLink invokeFuzzy(Class<Object> type, int args, int index) {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.holder.getDeclaredMethods())
            if (method.getReturnType().equals(type) && method.getParameterTypes().length == args)
                methods.add(method);
        
        this.method = ReflectAccess.accessMethod(methods.get(index));

        return this;
    }

    @Override public MethodLink invokeFuzzy(Class<Object> type, int index) {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.holder.getDeclaredMethods())
            if (method.getReturnType().equals(type))
                methods.add(method);

        this.method = ReflectAccess.accessMethod(methods.get(index));

        return this;
    }

    @Override public MethodLink invokeFuzzy(Class[] args, int index) {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.holder.getDeclaredMethods())
            if (Arrays.equals(method.getParameterTypes(), args))
                methods.add(method);

        this.method = ReflectAccess.accessMethod(methods.get(index));

        return this;
    }

    @Override public MethodLink invokeFuzzy(int args, int index) {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.holder.getDeclaredMethods())
            if (method.getParameterTypes().length == args)
                methods.add(method);

        this.method = ReflectAccess.accessMethod(methods.get(index));

        return this;
    }
    
    private class InvokerImpl implements MethodLink.Invoker {
        @Override public ReflectionChain invoke(Object instance) {
            MethodLinkImpl.this.parent.returned.add(MethodLinkImpl.this.method.invoke(instance,
                                                                                      this.processArgs(params)));
            return MethodLinkImpl.this.parent;
        }

        private Object[] processArgs(Object... args) {
            Collection<Object> list = new ArrayList<>();
            Collections.addAll(list, args);

            return list.toArray();
        }
    }
}

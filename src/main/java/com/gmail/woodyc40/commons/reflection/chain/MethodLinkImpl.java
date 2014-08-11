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

package com.gmail.woodyc40.commons.reflection.chain;

import com.gmail.woodyc40.commons.reflection.MethodManager;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;
import com.gmail.woodyc40.commons.reflection.impl.ReflectAccess;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Implementation of {@link com.gmail.woodyc40.commons.reflection.chain.MethodLink}, see description of the implemented
 * interfaces.
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
public class MethodLinkImpl implements MethodLink {
    private final Class<?>        holder;
    private final ReflectionChain parent;
    private final List<Object> params = new ArrayList<>();
    private MethodManager<Object, Object> method;

    @Override public MethodLink last(int index) {
        this.params.add(this.parent.returned.get(index));
        return this;
    }

    @Override public MethodLink param(Object... obj) {
        this.params.addAll(Arrays.asList(obj));
        return this;
    }

    @Override public MethodLink.Invoker invoker() {
        return new InvokerImpl();
    }

    @Override public MethodLink method(String name, Class... args) {
        this.method = ReflectAccess.accessMethod(ReflectionTool.forMethod(name, this.holder, args));
        return this;
    }

    @Override public MethodLink methodFuzzy(Class<Object> type, int args, int index) {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.holder.getDeclaredMethods())
            if (method.getReturnType().equals(type) && method.getParameterTypes().length == args)
                methods.add(method);

        this.method = ReflectAccess.accessMethod(methods.get(index));

        return this;
    }

    @Override public MethodLink methodFuzzy(Class<Object> type, int index) {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.holder.getDeclaredMethods())
            if (method.getReturnType().equals(type))
                methods.add(method);

        this.method = ReflectAccess.accessMethod(methods.get(index));

        return this;
    }

    @Override public MethodLink methodFuzzy(Class[] args, int index) {
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

    @Override public MethodManager<?, ?> getManager() {
        return this.method;
    }

    private class InvokerImpl implements MethodLink.Invoker {
        @Override public ReflectionChain invoke() {
            MethodLinkImpl.this.parent.returned.add(
                    MethodLinkImpl.this.method.invoke(MethodLinkImpl.this.params.get(0),
                                                      this.processArgs()));
            return MethodLinkImpl.this.parent;
        }

        private Object[] processArgs() {
            Object[] objects = new Object[MethodLinkImpl.this.params.size() - 1];
            for (int i = 1; i < MethodLinkImpl.this.params.size(); i++) {
                objects[i - 1] = MethodLinkImpl.this.params.get(i);
            }

            return objects;
        }
    }
}

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

import com.gmail.woodyc40.commons.reflection.ConstructorManager;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;
import com.gmail.woodyc40.commons.reflection.impl.ReflectAccess;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Implementation for {@link com.gmail.woodyc40.commons.reflection.chain.ConstructLink}. See class description of
 * implemented interface.
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
public class ConstructLinkImpl implements ConstructLink {
    private final Class<?>        base;
    private final ReflectionChain parent;
    private final Collection<Object> params = new ArrayList<>();
    private ConstructorManager<?> constructor;

    @Override public ConstructLink last(int index) {
        this.params.add(this.parent.returned.get(index).getClass());
        return this;
    }

    @Override public ConstructLink param(Object... obj) {
        Collections.addAll(this.params, obj);
        return this;
    }

    @Override public ConstructLink.Creator creator() {
        return new CreatorImpl();
    }

    @Override public ConstructLink construct(Class... params) {
        this.constructor = ReflectAccess.accessConstructor(ReflectionTool.forConstruct(this.base, params));
        return this;
    }

    @Override public ConstructLink constructFuzzy(int params, int index) {
        List<Constructor<?>> list = new ArrayList<>();
        for (Constructor<?> c : this.base.getDeclaredConstructors())
            if (c.getParameterTypes().length == params)
                list.add(c);
        this.constructor = ReflectAccess.accessConstructor(list.get(index));

        return this;
    }

    @Override public ConstructorManager<?> getManager() {
        return this.constructor;
    }

    private class CreatorImpl implements ConstructLink.Creator {
        @Override public ReflectionChain create() {
            return null;
        }
    }
}

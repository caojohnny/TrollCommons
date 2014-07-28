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

import com.gmail.woodyc40.commons.reflection.FieldManager;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;
import com.gmail.woodyc40.commons.reflection.impl.ReflectAccess;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FieldLinkImpl implements FieldLink {
    private final Class<?> base;
    private final ReflectionChain parent;
    private FieldManager<Object, Object> field;
    private Object instance;
    
    @Override public FieldLink last(int index) {
        this.instance = this.parent.returned.get(index);
        return this;
    }

    @Override public FieldLink instance(Object instance) {
        this.instance = instance;
        return this;
    }

    @Override public FieldLink.Getter getter() {
        return new GetterImpl();
    }

    @Override public FieldLink.Setter setter() {
        return new SetterImpl();
    }

    @Override public FieldLink field(String name) {
        this.field = ReflectAccess.accessField(ReflectionTool.forField(name, this.base));
        return this;
    }

    @Override public FieldLink fieldFuzzy(Class<?> type, int index) {
        List<Field> list = new ArrayList<>();
        for (Field field : this.base.getDeclaredFields())
            if (field.getType().equals(type))
                list.add(field);
        this.field = ReflectAccess.accessField(list.get(index));
        
        return this;
    }

    private class GetterImpl implements FieldLink.Getter {
        @Override public ReflectionChain get() {
            FieldLinkImpl.this.parent.returned.add(FieldLinkImpl.this.field.get(FieldLinkImpl.this.instance));
            return FieldLinkImpl.this.parent;
        }
    }

    private class SetterImpl implements FieldLink.Setter {
        @Override public ReflectionChain set(Object value) {
            FieldLinkImpl.this.field.set(FieldLinkImpl.this.instance, value);
            return FieldLinkImpl.this.parent;
        }
    }
}

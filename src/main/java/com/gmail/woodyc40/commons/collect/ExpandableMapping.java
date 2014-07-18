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

package com.gmail.woodyc40.commons.collect;

import java.util.HashMap;
import java.util.Map;

// TODO doc
public class ExpandableMapping<Key, VK> {
    private final Map<Key, Map<Object, VK>> mapping = new HashMap<>();

    public void put(Key key, VK vk, Object value) {
        Map<Object, VK> map = this.mapping.get(key);
        if (map == null)
            map = new HashMap<>();

        map.put(value, vk);
        this.mapping.put(key, map);
    }

    public Object get(Key key, VK vk) {
        return this.mapping.get(key).get(vk);
    }

    public void removeKeyAndValues(Key key) {
        this.mapping.remove(key);
    }

    public void removeValue(Object val) {
        for (Map.Entry<Key, Map<Object, VK>> entry : this.mapping.entrySet()) {
            entry.getValue().remove(val);
        }
    }

    public boolean containsKey(Key key) {
        return this.mapping.containsKey(key);
    }

    public boolean containsValue(Object val) {
        boolean[] contains = { false };
        for (Map.Entry<Key, Map<Object, VK>> entry : this.mapping.entrySet()) {
            if (entry.getValue().containsKey(val))
                contains[0] = true;
        }
        return contains[0];
    }
}
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

/**
 * A hashtable implementation that can support multiple values set to a specific key
 *
 * @param <Key> the key type to use
 * @param <VK>  the multi value type
 * @author AgentTroll
 * @version 1.0
 */
public class ExpandableMapping<Key, VK> {
    /** Storage */
    private final Map<Key, Map<Object, VK>> mapping = new HashMap<>();

    /**
     * Puts an entry into the mapping
     *
     * @param key   the key to use
     * @param vk    the value key to use
     * @param value the value
     */
    public void put(Key key, VK vk, Object value) {
        Map<Object, VK> map = this.mapping.get(key);
        if (map == null)
            map = new HashMap<>();

        map.put(value, vk);
        this.mapping.put(key, map);
    }

    /**
     * Retrieve operation
     *
     * @param key the key to use
     * @param vk  the value key to use
     * @return the value associated with the two keys
     */
    public VK get(Key key, Object vk) {
        Map<Object, VK> map = this.mapping.get(key);
        if (map == null) return null;

        return map.get(vk);
    }

    /**
     * Removes all values associated with the key
     *
     * @param key the key to remove all values associated with
     */
    public void removeKeyAndValues(Key key) {
        this.mapping.remove(key);
    }

    /**
     * Removes the value key and the value from the mapping
     *
     * @param val the value key to use
     */
    public void removeValue(Object val) {
        for (Map.Entry<Key, Map<Object, VK>> entry : this.mapping.entrySet()) {
            entry.getValue().remove(val);
        }
    }

    /**
     * Checks if the mapping contains the key
     *
     * @param key the key to use
     * @return {@code true} if the mapping has the key, {@code false} if not
     */
    public boolean containsKey(Key key) {
        return this.mapping.containsKey(key);
    }

    /**
     * Checks if the mapping contains the value
     *
     * @param val the val to use
     * @return {@code true} if the mapping has the value, {@code false} if not
     */
    public boolean containsValue(Object val) {
        boolean[] contains = { false };
        for (Map.Entry<Key, Map<Object, VK>> entry : this.mapping.entrySet()) {
            if (entry.getValue().containsKey(val))
                contains[0] = true;
        }
        return contains[0];
    }

    /**
     * Removes all entries from the mapping
     */
    public void clear() {
        this.mapping.clear();
    }
}
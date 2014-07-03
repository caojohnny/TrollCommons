/*
 * This file is part of BukkitCommons.
 *
 * Copyright (C) 2014 AgentTroll
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: woodyc40 (at) gmail (dot) com
 */

package com.gmail.woodyc40.commons.collect;

import java.util.HashMap;
import java.util.Map;

// TODO doc
public class ExpandableMapping<Key, VK> {
    private final Map<Key, Map<VK, Object>> mapping = new HashMap<>();

    public void put(Key key, VK vk, Object value) {
        Map<VK, Object> map = this.mapping.get(key);
        if (map == null)
            map = new HashMap<>();

        map.put(vk, value);
        this.mapping.put(key, map);
    }

    public Object get(Key key, VK vk) {
        return this.mapping.get(key).get(vk);
    }

    public void removeKeyAndValues(Key key) {
        this.mapping.remove(key);
    }

    public void removeValue(Object val) {
        this.mapping.forEach((k, v) -> v.forEach((kk, vv) -> {
            if (vv.equals(val))
                v.remove(kk);
        }));
    }

    public boolean containsKey(Key key) {
        return this.mapping.containsKey(key);
    }

    public boolean containsValue(Object val) {
        boolean[] contains = { false };
        this.mapping.forEach((k, v) -> {
            if (v.containsValue(val))
                contains[0] = true;
        });
        return contains[0];
    }
}
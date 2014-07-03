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
public class HashingCache<K, V> implements Cache<K, V> {
    private final Map<K, V> mapping = new HashMap<>();

    @Override public V lookup(K k) {
        return this.mapping.get(k);
    }

    @Override public V insert(K k, V v) {
        return this.mapping.put(k, v);
    }
}

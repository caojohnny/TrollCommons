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

import lombok.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Base structure for building iterators on
 *
 * @param <E> the element type that the iterator iterates over
 * @author AgentTroll
 * @version 1.0
 * @see java.util.Iterator
 */
@ToString @EqualsAndHashCode
public abstract class AbstractIterator<E> implements Iterator<E> {
    /** The values to iterate over */
    @Getter(AccessLevel.PROTECTED) private final Object[] values = this.allVals();
    /** Maximum index of the value array */
    @Getter(AccessLevel.PROTECTED) private final int      max    = this.allVals().length - 1;
    /** The current index of the iterator */
    @Getter(AccessLevel.PROTECTED) private int index;

    /**
     * The value array of elements
     *
     * @return the elements to iterate over
     */
    protected abstract Object[] allVals();

    /**
     * Removal hook
     *
     * @param val the value requested for removal
     */
    protected abstract void removeValue(E val);

    @Override public boolean hasNext() {
        return this.index + 1 <= this.max;
    }

    @Override public E next() {
        if (this.index + 1 > this.max)
            throw new NoSuchElementException();
        return (E) this.values[this.index++];
    }

    @Override public void remove() {
        this.removeValue((E) this.values[this.index]);
    }
}

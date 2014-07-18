package com.gmail.woodyc40.commons.collect;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Iterator;

public abstract class AbstractIterator<E> implements Iterator<E> {
    @Getter(AccessLevel.PROTECTED) private final Object[] values = this.allVals();
    @Getter(AccessLevel.PROTECTED) private final int max = this.allVals().length - 1;
    @Getter(AccessLevel.PROTECTED) private int index;

    protected abstract Object[] allVals();

    protected abstract void removeValue(E val);

    @Override public boolean hasNext() {
        return this.index + 1 <= this.max;
    }

    @Override public E next() {
        return (E) this.values[this.index++];
    }

    @Override public void remove() {
       this.removeValue((E) this.values[this.index]);
    }
}

package com.gmail.woodyc40.commons.misc;

public interface MultiParamRunnable<R, T, V> {
    R run(T t, V v);
}

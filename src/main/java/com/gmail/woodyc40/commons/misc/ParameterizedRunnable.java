package com.gmail.woodyc40.commons.misc;

public interface ParameterizedRunnable<R, T> {
    R run(T t);
}

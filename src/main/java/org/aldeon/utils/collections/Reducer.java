package org.aldeon.utils.collections;

public interface Reducer<T> {
    T reduce(T a, T b);
}

package org.aldeon.utils.various;

import java.util.Comparator;

public interface Arithmetic<T> extends Comparator<T> {
    T add(T a, T b);
    T sub(T a, T b);
}

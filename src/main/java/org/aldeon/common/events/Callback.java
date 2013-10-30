package org.aldeon.common.events;

public interface Callback<T> {
    void call(T val);
}

package org.aldeon.events;

public interface Callback<T> {
    void call(T val);
}

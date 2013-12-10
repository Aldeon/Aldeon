package org.aldeon.utils.helpers;

import org.aldeon.events.Callback;

public class Callbacks {
    public static <T> Callback<T> emptyCallback() {
        return new Callback<T>() {
            @Override
            public void call(T val) {
                // do nothing
            }
        };
    }
}

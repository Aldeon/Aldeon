package org.aldeon.events;

import java.util.concurrent.Executor;

public class CallbackAndExecutor<T> implements AsyncCallback<T> {

    private final Callback<T> callback;
    private final Executor executor;

    public CallbackAndExecutor(Callback<T> callback, Executor executor) {
        this.callback = callback;
        this.executor = executor;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public void call(final T val) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.call(val);
            }
        });
    }
}

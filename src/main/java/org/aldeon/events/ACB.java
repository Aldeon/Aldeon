package org.aldeon.events;

import java.util.concurrent.Executor;

public abstract class ACB<T> implements AsyncCallback<T> {

    private final Executor executor;

    public ACB(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public final void call(final T val) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                react(val);
            }
        });
    }

    protected abstract void react(T val);
}

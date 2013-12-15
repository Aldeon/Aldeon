package org.aldeon.events;


public class SingleRunCallback<T> implements Callback<T> {

    private final Callback<T> callback;
    private boolean notYetCalled = true;

    public SingleRunCallback(Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public synchronized void call(T val) {
        if(notYetCalled) {
            notYetCalled = false;
            callback.call(val);
        }
    }
}

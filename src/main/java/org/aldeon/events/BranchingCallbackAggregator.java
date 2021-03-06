package org.aldeon.events;

import org.aldeon.utils.various.Reducer;

public class BranchingCallbackAggregator<T> {

    private final Reducer<T> reducer;
    private Callback<T> callback;

    private int childrenGiven = 0;
    private int childrenEnded = 0;
    private boolean started = false;
    private boolean called = false;
    private T result = null;

    public BranchingCallbackAggregator(Reducer<T> reducer, Callback<T> callback) {
        this.reducer = reducer;
        this.callback = callback;
    }

    public synchronized void start(T val) {
        started = true;
        checkAndCall(val);
    }

    public synchronized Callback<T> childCallback() {
        childrenGiven++;
        return new Callback<T>() {

            private boolean callbackCalled = false;

            @Override
            public synchronized void call(T val) {
                if(!callbackCalled) {
                    callbackCalled = true;
                    onChildCalled(val);
                }
            }
        };
    }

    private synchronized void onChildCalled(T val) {
        childrenEnded++;
        checkAndCall(val);
    }

    private void checkAndCall(T val) {
        if(result == null) {
            result = val;
        } else {
            result = reducer.reduce(result, val);
        }
        if(started && !called && childrenGiven == childrenEnded) {
            called = true;
            callback.call(result);
        }
    }
}

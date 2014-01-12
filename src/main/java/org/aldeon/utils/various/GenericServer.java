package org.aldeon.utils.various;

import org.aldeon.core.services.Service;

public abstract class GenericServer<T> implements Service {

    private boolean started = false;
    private boolean ended = false;

    protected abstract void handle(T data);
    protected abstract T provide();
    protected abstract boolean initialize();
    protected abstract void finalize();

    @Override
    public void start() {
        if(!started) {
            started = true;
            if(initialize()) {
                Runnable worker = new Runnable() {
                    @Override
                    public void run() {
                        T data;
                        while(started && !ended) {
                            data = provide();
                            if(data != null) {
                                handle(data);
                            }
                        }
                    }
                };
                new Thread(worker).start();
            }
        }
    }

    @Override
    public void close() {
        if(!ended) {
            finalize();
            ended = true;
        }
    }
}

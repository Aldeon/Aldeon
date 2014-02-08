package org.aldeon.utils.various;

import org.aldeon.core.services.Service;

public class LoopWorker implements Service {

    private final Runnable worker;
    private boolean working = false;
    private boolean keepWorking = false;
    private Thread thread;

    public LoopWorker(final long intervalMilliseconds, final Runnable task) {

        worker = new Runnable() {

            @Override
            public void run() {
                while(keepWorking) {
                    task.run();
                    try {
                        Thread.sleep(intervalMilliseconds);
                    } catch (InterruptedException e) {
                        // closing?
                    }
                }
                working = false;
            }
        };
    }

    public boolean isWorking() {
        return isWorking();
    }

    @Override
    public void start() {
        if(!working) {
            working = true;
            keepWorking = true;
            thread = new Thread(worker);
            thread.start();
        }
    }

    @Override
    public void close() {
        if(working && keepWorking) {
            keepWorking = false;
            working = false;
            thread.interrupt();
        }
    }
}

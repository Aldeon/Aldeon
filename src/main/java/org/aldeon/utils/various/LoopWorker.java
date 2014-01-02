package org.aldeon.utils.various;

import org.aldeon.model.Service;

public class LoopWorker implements Service {

    private final Runnable worker;
    private boolean working = false;
    private boolean keepWorking = false;

    public LoopWorker(final long intervalMilis, final Runnable task) {

        worker = new Runnable() {

            @Override
            public void run() {
                while(keepWorking) {
                    task.run();
                    try {
                        Thread.sleep(intervalMilis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
        working = true;
        keepWorking = true;
        new Thread(worker).start();
    }

    @Override
    public void close() {
        keepWorking = false;
    }
}

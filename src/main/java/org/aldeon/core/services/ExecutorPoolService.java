package org.aldeon.core.services;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorPoolService implements Executor, Service {

    private ExecutorService underlyingService;

    public ExecutorPoolService(int threads) {
        underlyingService = Executors.newFixedThreadPool(threads);
    }

    @Override
    public void execute(Runnable command) {
        underlyingService.execute(command);
    }

    @Override
    public void start() {
        // do nothing - already running
    }

    @Override
    public void close() {
        underlyingService.shutdown();
    }
}

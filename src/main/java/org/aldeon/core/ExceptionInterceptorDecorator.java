package org.aldeon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;


public class ExceptionInterceptorDecorator implements Executor {

    private static final Logger log = LoggerFactory.getLogger(Executor.class);

    private final Executor executor;

    public ExceptionInterceptorDecorator(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(final Runnable command) {

        System.out.println("Executing task " + command);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } catch (Exception e) {
                    log.error("Executor task failed", e);
                }
            }
        });
    }
}

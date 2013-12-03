package org.aldeon.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class ThrowableInterceptor implements Executor {

    private static final Logger log = LoggerFactory.getLogger(ThrowableInterceptor.class);

    private final Executor executor;

    public ThrowableInterceptor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(final Runnable command) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } catch (Throwable throwable) {
                    log.error("Unhandled exception", throwable);
                }
            }
        });
    }
}

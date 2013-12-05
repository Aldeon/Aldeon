package org.aldeon.events.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class ExecutorLogger implements Executor {

    private static final Logger log = LoggerFactory.getLogger(ExecutorLogger.class);

    private final Executor executor;
    private final String label;

    public ExecutorLogger(String label, Executor executor) {
        this.executor = executor;
        this.label = label;
    }

    @Override
    public void execute(Runnable command) {

        log.info("Executor <" + label + ">: " + sourceFile(command));

        executor.execute(command);
    }

    private String sourceFile(Object obj) {
        return obj.toString();
    }
}

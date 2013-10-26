package org.aldeon.common.nio;

public interface Service {
    /**
     * Starts the service.
     */
    void start();

    /**
     * Closes the service. All connections are closed and all tasks are discarded.
     */

    void close();
}

package org.aldeon.common.communication;

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

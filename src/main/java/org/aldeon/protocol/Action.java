package org.aldeon.protocol;


import org.aldeon.events.Callback;

import java.util.concurrent.Executor;

/**
 * Aggregates logic related to reacting to an incoming message.
 * @param <R>
 */
public interface Action<R extends Request> {

    /**
     * Trigger the process of generating a response.
     * @param request request we respond to
     * @param onResponse callback to be called when the response is ready
     * @param executor provides a thread pool to execute processor-heavy tasks, if necessary.
     */
    public void respond(R request, Callback<Response> onResponse, Executor executor);
}
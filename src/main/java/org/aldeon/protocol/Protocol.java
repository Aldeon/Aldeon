package org.aldeon.protocol;

import org.aldeon.events.Callback;

import java.util.concurrent.Executor;

/**
 * Responsible for creating appropriate responses to each request.
 */
public interface Protocol {
    /**
     * Accept the request and generate the response in near future.
     * @param request request to respond to
     * @param onResponse short and non-blocking callback to be called when the response is ready.
     * @param executor where the callback should be called
     */
    void createResponse(Request request, Callback<Response> onResponse, Executor executor);
}

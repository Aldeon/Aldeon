package org.aldeon.protocol;

import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;

/**
 * Responsible for creating appropriate responses to each request.
 */
public interface Protocol {
    /**
     * Accept the request and generate the response in near future.
     * @param request request to respond to
     * @param onResponse short and non-blocking callback to be called when the response is ready.
     * @param executor provides a thread pool to execute processor-heavy tasks, if necessary.
     */
    void createResponse(PeerAddress peer, Request request, AsyncCallback<Response> onResponse);
}

package org.aldeon.protocol;

import org.aldeon.events.Callback;
import org.aldeon.networking.common.PeerAddress;

/**
 * Responsible for creating appropriate responses to each request.
 */
public interface Protocol {
    /**
     * Accept the request and generate the response in near future.
     * @param request request to respond to
     * @param onResponse short and non-blocking callback to be called when the response is ready.
     */
    void createResponse(PeerAddress peer, Request request, Callback<Response> onResponse);
}

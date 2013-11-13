package org.aldeon.protocol;


import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;

/**
 * Aggregates logic related to reacting to an incoming message.
 * @param <R>
 */
public interface Action<R extends Request> {

    /**
     * Trigger the process of generating a response.
     * @param request request we respond to
     * @param onResponse callback to be called when the response is ready
     */
    public void respond(PeerAddress peer, R request, AsyncCallback<Response> onResponse);
}
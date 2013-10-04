package org.aldeon.common;


/**
 * Communication endpoint, made available for other peers to use
 */
public interface Endpoint {

    public void setResponder(Responder responder);

    public void start();
    public void stop();

}

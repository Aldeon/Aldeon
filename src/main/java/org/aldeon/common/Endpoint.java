package org.aldeon.common;


/**
 * Communication endpoint, made available for other peers to use
 */
public interface Endpoint {

    public void setObserver(Observer observer);

    public void start();
    public void stop();

}

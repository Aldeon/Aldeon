package org.aldeon.common;


import org.aldeon.common.net.ConnectionPolicy;

/**
 * Endpoint with some port requirements
 */
public interface EndpointWithConnectionPolicy extends Endpoint {

    public void setConnectionPolicy(ConnectionPolicy connectionPolicy);
    public ConnectionPolicy getConnectionPolicy();

}

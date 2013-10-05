package org.aldeon.common;


import org.aldeon.common.net.PortPolicy;

/**
 * Endpoint with some port requirements
 */
public interface EndpointWithPortPolicy extends Endpoint {

    public void setPortPolicy(PortPolicy portPolicy);
    public PortPolicy getPortPolicy();

}

package org.aldeon.common;


import org.aldeon.common.net.AddressTranslation;

/**
 * Endpoint with some port requirements
 */
public interface EndpointWithAddressTranslation extends Endpoint {

    public void setAddressTranslation(AddressTranslation addressTranslation);
    public AddressTranslation getAddressTranslation();

}

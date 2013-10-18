package org.aldeon.nat;

import org.aldeon.common.net.AddressTranslation;

public interface AddressTranslationFactory {
    public void begin();
    public void abort();
    public boolean isReady();
    public AddressTranslation getAddressTranslation();
}

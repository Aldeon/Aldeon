package org.aldeon.common.net;

public interface AddressTranslationFactory {
    public void begin();
    public void abort();
    public boolean isReady();
    public AddressTranslation getAddressTranslation();
}

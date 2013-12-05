package org.aldeon.nat;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import org.aldeon.nat.utils.NoAddressTranslation;
import org.aldeon.net.AddressTranslation;
import org.aldeon.utils.net.PortImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressTranslationModule extends AbstractModule implements Provider<AddressTranslation> {

    @Override
    protected void configure() {

    }

    @Override
    public AddressTranslation get() {
        try {
            return new NoAddressTranslation(new PortImpl(8080), InetAddress.getByName("0.0.0.0"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}

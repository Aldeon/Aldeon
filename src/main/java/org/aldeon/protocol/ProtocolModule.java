package org.aldeon.protocol;

import com.google.inject.AbstractModule;

public class ProtocolModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    public static Protocol createProtocol() {
        return new ProtocolImpl();
    }
}

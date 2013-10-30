package org.aldeon.protocol;

import com.google.inject.AbstractModule;
import org.aldeon.core.Core;

public class ProtocolModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    public static Protocol createProtocol(Core core) {
        return new ProtocolImpl(core);
    }
}

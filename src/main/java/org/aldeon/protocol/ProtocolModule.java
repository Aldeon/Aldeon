package org.aldeon.protocol;

import com.google.inject.AbstractModule;
import org.aldeon.common.core.Core;
import org.aldeon.common.protocol.Protocol;

public class ProtocolModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    public static Protocol createProtocol(Core core) {
        return new ProtocolImpl(core);
    }
}

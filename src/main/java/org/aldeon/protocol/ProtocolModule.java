package org.aldeon.protocol;

import com.google.inject.AbstractModule;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.*;
import org.aldeon.events.ACB;

public class ProtocolModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    private static Protocol createProtocol() {
        return new ProtocolImpl();
    }

    public static void initialize() {

        // Instantiate the protocol
        final Protocol protocol = createProtocol();

        Core core = CoreModule.getInstance();

        // Handle incoming requests
        core.getEventLoop().assign(InboundRequestEvent.class, new ACB<InboundRequestEvent>(core.serverSideExecutor()) {

            // A wild request appears

            @Override
            protected void react(final InboundRequestEvent event) {

                // MB used protocol

                protocol.createResponse(event.getTask().getAddress(), event.getTask().getRequest(), new ACB<Response>(getExecutor()) {

                    @Override
                    protected void react(Response response) {

                        // It's super effective

                        event.getTask().sendResponse(response);
                    }
                });
            }
        });
    }
}

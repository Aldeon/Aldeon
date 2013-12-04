package org.aldeon.protocol;

import com.google.inject.AbstractModule;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.InboundRequestEvent;
import org.aldeon.events.ACB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(ProtocolModule.class);

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

            @Override
            protected void react(final InboundRequestEvent event) {

                try {
                    protocol.createResponse(event.getTask().getAddress(), event.getTask().getRequest(), new ACB<Response>(getExecutor()) {

                        @Override
                        protected void react(Response response) {
                            event.getTask().sendResponse(response);
                        }
                    });
                } catch (Exception e) {
                    log.warn("Failed to create a response", e);
                    event.getTask().sendResponse(null);
                }
            }
        });
    }
}

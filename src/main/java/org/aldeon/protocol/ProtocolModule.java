package org.aldeon.protocol;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.InboundRequestEvent;
import org.aldeon.events.ACB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolModule extends AbstractModule implements Provider<Protocol> {

    private static final Logger log = LoggerFactory.getLogger(ProtocolModule.class);

    @Override
    protected void configure() {
        bind(Protocol.class).to(ActionsBasedProtocol.class);
        bind(Core.class).toProvider(CoreModule.class);
    }

    @Override
    public Protocol get() {
        return Guice.createInjector(this).getInstance(Protocol.class);
    }

    public static void initialize() {

        // Instantiate the protocol
        final Protocol protocol = new ProtocolModule().get();

        Core core = CoreModule.getInstance();

        // Handle incoming requests
        core.getEventLoop().assign(InboundRequestEvent.class, new ACB<InboundRequestEvent>(core.serverSideExecutor()) {

            @Override
            protected void react(final InboundRequestEvent event) {

                try {
                    protocol.createResponse(event.getTask().getAddress(), event.getTask().getRequest(), new ACB<Response>(getExecutor()) {

                        @Override
                        protected void react(Response response) {

                            log.info("Generated response: " + response);

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

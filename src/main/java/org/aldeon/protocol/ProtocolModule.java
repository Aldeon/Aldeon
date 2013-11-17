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

            /*
                A wild request appears
             */

            @Override
            protected void react(final InboundRequestEvent event) {

                /*
                    MB used protocol
                 */

                protocol.createResponse(event.getTask().getAddress(), event.getTask().getRequest(), new ACB<Response>(getExecutor()) {

                    @Override
                    protected void react(Response response) {

                        /*
                            It's super effective
                         */

                        event.getTask().sendResponse(response);
                    }
                });
            }
        });

        //Topic removed -> delete root + all children
        core.getEventLoop().assign(TopicRemovedEvent.class, new ACB<TopicRemovedEvent>(core.serverSideExecutor()) {
            @Override
            protected void react(final TopicRemovedEvent event) {
                //Delete topic + go back if currently browsing this topic
                System.out.println("Topic removed");
            }
        });

        //New message -> show it in GUI
        core.getEventLoop().assign(InboundMessageEvent.class, new ACB<InboundMessageEvent>(core.serverSideExecutor()) {
            @Override
            protected void react(final InboundMessageEvent event) {
                //Inform GUI about new message
                System.out.println("New message: "+event.getMessage().getContent());
            }
        });

        //Remove message -> remove from GUI
        core.getEventLoop().assign(RemoveMessageEvent.class, new ACB<RemoveMessageEvent>(core.serverSideExecutor()){
            @Override
            protected void react(RemoveMessageEvent event) {
            //Delete message from GUI
                System.out.println("Delete message");
            }
        });

        //Identity added -> show it in GUI
        core.getEventLoop().assign(IdentityAddedEvent.class, new ACB<IdentityAddedEvent>(core.serverSideExecutor()) {
            @Override
            protected void react(final IdentityAddedEvent event) {
                //Inform GUI about new id
                System.out.println("New identity");
            }
        });

        //Identity removed -> remove it from GUI
        core.getEventLoop().assign(IdentityRemovedEvent.class, new ACB<IdentityRemovedEvent>(core.serverSideExecutor()) {
            @Override
            protected void react(final IdentityRemovedEvent event) {
                //Delete identity from GUI
                System.out.println("Removed identity");
            }
        });

    }
}

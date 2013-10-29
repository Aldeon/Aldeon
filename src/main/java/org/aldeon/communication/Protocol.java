package org.aldeon.communication;

import org.aldeon.common.Observer;
import org.aldeon.protocol.action.Action;
import org.aldeon.protocol.action.ExampleAction;
import org.aldeon.protocol.query.ExampleQuery;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 */
public class Protocol<Address> {
    private ICommunicationProvider<Address> commProvider;
    private Map<Class<?>, Action<? extends Query, ?>> actions;


    public Protocol(ICommunicationProvider<Address> commProvider) {
        this.commProvider = commProvider;
    }
    public ProtocolMessage receive(ProtocolMessage protocolMessage) {
        return protocolMessage.onReceive();
    }

    public ProtocolMessage send(ProtocolMessage protocolMessage, Address recipientAddr) {
        return commProvider.send(protocolMessage, recipientAddr);
    }

    public Protocol() {
        actions = new HashMap<>();

        // Register all the actions here
        actions.put(ExampleQuery.class, new ExampleAction());
    }

    public Response respond(Query query, Observer observer) {

        Action<? extends Query,?> action = actions.get(query.getClass());

        if(action == null) {
            throw new IllegalArgumentException("Unknown query type");
        } else {
            return ((Action<Query, ?>) action).respond(query, observer); //the cast here is fine - Query is abstract
        }
    }
}


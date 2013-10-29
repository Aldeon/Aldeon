package org.aldeon.communication.http_provider.jetty_receiver;

import org.aldeon.communication.CommunicationProvider;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.http_provider.IPIdentifier;

/**
 *
 */
public class JettyReceiver implements Receiver<String, IPIdentifier> {

    private CommunicationProvider<String, IPIdentifier> communicationProvider;
    private JettyEndpointHandler handler;
    private JettyHttpEndpoint endpoint;

    public JettyReceiver(JettyEndpointHandler handler, JettyHttpEndpoint endpoint) {
        this.handler = handler;
        this.endpoint = endpoint;
    }
    @Override
    public void setCommunicationProvider(CommunicationProvider<String, IPIdentifier> communicationProvider) {
        this.communicationProvider = communicationProvider;
    }

    @Override
    public void start() {
        endpoint.start();
    }

    @Override
    public void stop() {
        endpoint.stop();
    }

    String receive(String msg) {
        return communicationProvider.receive(msg);
    }
}

//send -> recvResponse
//listen -> recvRequest

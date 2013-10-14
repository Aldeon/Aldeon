package org.aldeon.communication.http_provider.jetty_receiver;

import org.aldeon.communication.CommunicationProvider;
import org.aldeon.communication.Receiver;

/**
 *
 */
public class JettyReceiver implements Receiver<String> {

    private CommunicationProvider<String> communicationProvider;

    @Override
    public void setCommunicationProvider(CommunicationProvider<String> communicationProvider) {
        this.communicationProvider = communicationProvider;
    }


}

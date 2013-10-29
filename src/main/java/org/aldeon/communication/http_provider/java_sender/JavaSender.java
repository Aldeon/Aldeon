package org.aldeon.communication.http_provider.java_sender;

import org.aldeon.communication.Sender;
import org.aldeon.communication.http_provider.IPIdentifier;

/**
 *
 */
public class JavaSender implements Sender<String, IPIdentifier> {
    @Override
    public String send(String formattedMessage, IPIdentifier recipientAddress) {
        System.out.println("sending string message");
        return null;
    }
}

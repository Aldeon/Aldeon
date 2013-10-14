package org.aldeon.communication.http_provider.java_sender;

import org.aldeon.communication.Sender;

/**
 *
 */
public class JavaSender implements Sender<String> {
    @Override
    public void send(String formattedMessage) {
        System.out.println("sending string message");
    }
}

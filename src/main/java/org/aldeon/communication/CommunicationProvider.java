package org.aldeon.communication;

import org.aldeon.net.PeerAddress;

public interface CommunicationProvider<T extends PeerAddress> {
    Sender<T> getSender();
    Receiver<T> getReceiver();
}

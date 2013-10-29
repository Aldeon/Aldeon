package org.aldeon.communication;

import org.aldeon.common.net.address.PeerAddress;

public interface CommunicationProvider<T extends PeerAddress> {
    Sender<T> getSender();
    Receiver<T> getReceiver();
}

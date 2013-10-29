package org.aldeon.common.core;

import org.aldeon.common.events.EventLoop;
import org.aldeon.common.model.Storage;
import org.aldeon.common.net.address.IdentifiablePeerAddress;
import org.aldeon.common.net.address.PeerAddress;
import org.aldeon.communication.CommunicationProvider;
import org.aldeon.dht.Dht;

public interface Core {
    <T extends PeerAddress> CommunicationProvider<T> getCommunicationProvider(Class<T> addressType);
    <T extends IdentifiablePeerAddress> Dht<T> getDht(Class<T> addressType);

    Storage getStorage();
    EventLoop getEventLoop();
}

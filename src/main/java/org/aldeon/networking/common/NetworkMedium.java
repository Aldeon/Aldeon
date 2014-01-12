package org.aldeon.networking.common;

import org.aldeon.core.services.Service;
import org.aldeon.networking.exceptions.AddressParseException;

import java.util.Set;

public interface NetworkMedium extends Service {

    Set<AddressType> addressTypes();

    SendPoint sendPoint();
    RecvPoint recvPoint();

    PeerAddress localAddressForRemoteAddress(PeerAddress peerAddress);
    Set<? extends PeerAddress> localAddresses();
    boolean remoteAddressBelievable(PeerAddress address);

    String serialize(PeerAddress address) throws AddressParseException;
    PeerAddress deserialize(String address) throws AddressParseException;
}

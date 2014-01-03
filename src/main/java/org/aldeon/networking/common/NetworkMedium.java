package org.aldeon.networking.common;

import org.aldeon.core.services.Service;
import org.aldeon.networking.exceptions.AddressParseException;
import org.aldeon.utils.json.ParseException;

import java.util.Set;

public interface NetworkMedium extends Service {

    Set<AddressType> addressTypes();

    SendPoint sendPoint();
    RecvPoint recvPoint();

    PeerAddress machineAddressForForeignAddress(PeerAddress peerAddress);

    String serialize(PeerAddress address) throws AddressParseException;
    PeerAddress deserialize(String address) throws AddressParseException;
}

package org.aldeon.networking.common;

import org.aldeon.networking.exceptions.AddressParseException;
import org.aldeon.utils.json.ParseException;

import java.util.Set;

public interface NetworkMedium {

    Set<AddressType> addressTypes();

    SendPoint sendPoint();
    RecvPoint recvPoint();

    Set<PeerAddress> getMachineAddresses(AddressType addressType);

    String serialize(PeerAddress address) throws AddressParseException;
    PeerAddress deserialize(String address) throws AddressParseException;
}

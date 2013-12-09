package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Request;

public class IndicateInterestRequest implements Request {

    public static String TYPE = "indicate_interest";

    public String type = TYPE;
    public Identifier topic;
    public PeerAddress address;

}

package org.aldeon.communication.http_provider;

import org.aldeon.communication.Converter;
import org.aldeon.communication.ProtocolMessage;

/**
 *
 */
public class JSONConverter implements Converter<String> {

    @Override
    public String encode(ProtocolMessage normalProtocolMessage) {
        return null;
    }

    @Override
    public ProtocolMessage decode(String formattedMessage) {
        return null;
    }
}

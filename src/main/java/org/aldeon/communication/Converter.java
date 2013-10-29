package org.aldeon.communication;

import org.aldeon.communication.ProtocolMessage;

/**
 *
 * Converts messages to format recognizable by Sender and Receiver.
 *
 */
public interface Converter <Format> {
    Format encode(ProtocolMessage normalProtocolMessage);
    ProtocolMessage decode(Format formattedMessage);
}

package org.aldeon.communication;

/**
 *
 * abstract type of message that is going to be used by Protocol
 *
 */
public abstract class ProtocolMessage {

    /**
     * Called by protocol, when message is received.
     *
     * @return ProtocolMessage which is going to be send back to sender
     */
    protected ProtocolMessage onReceiveCallback() {
        return null;
    }

    public ResponseCommand responseCommand;

    //package visibility - only abstract protocol can trigger this event
    ProtocolMessage onReceive() {
        return onReceiveCallback();
    }
}

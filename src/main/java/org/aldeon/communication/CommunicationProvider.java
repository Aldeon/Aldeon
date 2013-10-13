package org.aldeon.communication;

import com.google.inject.Inject;

/**
 *
 * Abstract concept of communication medium such as http requests via TCP/IP
 * Instance of this object is passed to protocol (so it can invoke send on this)
 * and to receiver (so it can invoke receive on this)
 *
 */
public class CommunicationProvider<Format extends Object> implements ICommunicationProvider{
    private final Converter<Format> converter;
    private final Receiver<Format> receiver;
    private final Sender<Format> sender;
    private Protocol protocol;

    @Inject
    public CommunicationProvider(Converter<Format> converter,
                                 Receiver<Format> receiver,
                                 Sender<Format> sender) {
        this.converter = converter;
        this.receiver = receiver;
        this.sender = sender;

        receiver.setCommunicationProvider(this);
    }

    public void send(ProtocolMessage protocolMessage) {
        sender.send(converter.encode(protocolMessage));
    }

    @Override
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public ProtocolMessage receive(Format formattedMessage) {
        return protocol.receive(converter.decode(formattedMessage));
    }


}

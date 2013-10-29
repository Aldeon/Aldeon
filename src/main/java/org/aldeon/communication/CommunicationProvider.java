package org.aldeon.communication;

import com.google.inject.Inject;

/**
 *
 * Abstract concept of communication medium such as http requests via TCP/IP
 * Instance of this object is passed to protocol (so it can invoke send on this)
 * and to receiver (so it can invoke receive on this)
 *
 */
public class CommunicationProvider<Format, Address> implements ICommunicationProvider<Address>{
    private final Converter<Format> converter;
    private final Receiver<Format, Address> receiver;
    private final Sender<Format, Address> sender;
    private Protocol protocol;

    @Inject
    public CommunicationProvider(Converter<Format> converter,
                                 Receiver<Format, Address> receiver,
                                 Sender<Format, Address> sender) {
        this.converter = converter;
        this.receiver = receiver;
        this.sender = sender;

        receiver.setCommunicationProvider(this);
    }

    @Override
    public ProtocolMessage send(ProtocolMessage protocolMessage, Address address) {
        return converter.decode(sender.send(converter.encode(protocolMessage), address));
    }

    @Override
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public ProtocolMessage receive(Format formattedMessage) {
        return protocol.receive(converter.decode(formattedMessage));
    }


}

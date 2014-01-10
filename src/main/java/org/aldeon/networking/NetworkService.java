package org.aldeon.networking;

import com.google.inject.Inject;
import org.aldeon.core.services.Service;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Receiver;
import org.aldeon.networking.common.Sender;
import org.aldeon.networking.conversion.ByteBufferToRequestConverter;
import org.aldeon.networking.conversion.ByteBufferToResponseConverter;
import org.aldeon.networking.conversion.RequestClassMapper;
import org.aldeon.networking.conversion.RequestToByteBufferConverter;
import org.aldeon.networking.conversion.ResponseClassMapper;
import org.aldeon.networking.conversion.ResponseToByteBufferConverter;
import org.aldeon.networking.exceptions.AddressParseException;
import org.aldeon.networking.wrappers.ReceiverDisptcher;
import org.aldeon.networking.wrappers.RecvPointBasedReceiver;
import org.aldeon.networking.wrappers.SendPointBasedSender;
import org.aldeon.networking.wrappers.SenderDispatcher;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.JsonModule;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetworkService implements Service {

    private final Sender unifiedSender;
    private final Receiver unifiedReceiver;
    private Map<AddressType, NetworkMedium> mediums = new HashMap<>();
    private Set<NetworkMedium> mediumSet;
    private Set<PeerAddress> localAddresses = new HashSet<>();

    @Inject
    public NetworkService(Set<NetworkMedium> mediumSet) {

        this.mediumSet = mediumSet;

        JsonParser parser = new JsonModule().get();
        ClassMapper<Request> requestMapper = new RequestClassMapper();
        ClassMapper<Response> responseMapper = new ResponseClassMapper();

        Converter<Request, ByteBuffer> requestEncoder = new RequestToByteBufferConverter(parser);
        Converter<ByteBuffer, Response> responseDecoder = new ByteBufferToResponseConverter(parser, responseMapper);
        Converter<ByteBuffer, Request> requestDecoder = new ByteBufferToRequestConverter(parser, requestMapper);
        Converter<Response, ByteBuffer> responseEncoder = new ResponseToByteBufferConverter(parser);

        Set<Sender> senders = new HashSet<>();
        Set<Receiver> receivers = new HashSet<>();

        for(NetworkMedium medium: mediumSet) {
            senders.add(new SendPointBasedSender(medium.sendPoint(), medium.addressTypes(), requestEncoder, responseDecoder));
            receivers.add(new RecvPointBasedReceiver(medium.recvPoint(), requestDecoder, responseEncoder));
            for(AddressType type: medium.addressTypes()) {
                mediums.put(type, medium);
            }
        }

        unifiedSender = new SenderDispatcher(senders);
        unifiedReceiver = new ReceiverDisptcher(receivers);
    }

    public Sender getUnifiedSender() {
        return unifiedSender;
    }

    public Receiver getUnifiedReceiver() {
        return unifiedReceiver;
    }

    public PeerAddress localAddress(PeerAddress peerAddress) {
        NetworkMedium medium = mediums.get(peerAddress.getType());
        if(medium != null) {
            return medium.localAddressForRemoteAddress(peerAddress);
        } else {
            return null;
        }
    }

    public Set<PeerAddress> getLocalAddresses() {
        return localAddresses;
    }

    public boolean addressBelieveable(PeerAddress peerAddress) {
        NetworkMedium medium = mediums.get(peerAddress.getType());
        if(medium != null) {
            return medium.remoteAddressBelievable(peerAddress);
        } else {
            return false;
        }
    }

    public String serialize(PeerAddress address) throws AddressParseException {
        NetworkMedium medium = mediums.get(address.getType());
        if(medium != null) {
            return medium.serialize(address);
        } else {
            throw new AddressParseException("Unknown address type");
        }
    }

    public PeerAddress deserialize(AddressType type, String string) throws AddressParseException {
        NetworkMedium medium = mediums.get(type);
        if(medium != null) {
            return medium.deserialize(string);
        } else {
            throw new AddressParseException("Unknown address type");
        }
    }

    @Override
    public void start() {
        for(NetworkMedium medium: mediumSet) {
            medium.start();
            localAddresses.addAll(medium.localAddresses());
        }
        localAddresses = Collections.unmodifiableSet(localAddresses);
    }

    @Override
    public void close() {
        for(NetworkMedium medium: mediumSet) {
            medium.close();
        }
    }
}

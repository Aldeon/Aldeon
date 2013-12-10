package org.aldeon.networking;

import com.google.inject.Inject;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetworkState {

    private final Sender unifiedSender;
    private final Receiver unifiedReceiver;
    private Set<Sender> senders = new HashSet<>();
    private Set<Receiver> receivers = new HashSet<>();
    private Map<AddressType, NetworkMedium> mediums = new HashMap<>();

    @Inject
    public NetworkState(Set<NetworkMedium> mediumSet) {

        JsonParser parser = new JsonModule().get();
        ClassMapper<Request> requestMapper = new RequestClassMapper();
        ClassMapper<Response> responseMapper = new ResponseClassMapper();

        Converter<Request, ByteBuffer> requestEncoder = new RequestToByteBufferConverter(parser);
        Converter<ByteBuffer, Response> responseDecoder = new ByteBufferToResponseConverter(parser, responseMapper);
        Converter<ByteBuffer, Request> requestDecoder = new ByteBufferToRequestConverter(parser, requestMapper);
        Converter<Response, ByteBuffer> responseEncoder = new ResponseToByteBufferConverter(parser);

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
        return  unifiedReceiver;
    }

    public PeerAddress getMachineAddress(AddressType type) {
        // TODO: Should this be ambiguous? (What about IP aliasing?)
        NetworkMedium medium = mediums.get(type);
        if(medium != null) {
            return medium.getMachineAddress(type);
        } else {
            return null;
        }
    }

    public String serialize(PeerAddress address) {
        NetworkMedium medium = mediums.get(address.getType());
        if(medium != null) {
            return medium.serialize(address);
        } else {
            return null;
        }
    }

    public PeerAddress deserialize(AddressType type, String string) {
        NetworkMedium medium = mediums.get(type);
        if(medium != null) {
            return medium.deserialize(string);
        } else {
            return null;
        }
    }

}

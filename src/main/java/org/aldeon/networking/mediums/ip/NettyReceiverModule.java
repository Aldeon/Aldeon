package org.aldeon.networking.mediums.ip;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import org.aldeon.networking.common.Receiver;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.conversion.ByteBufferToRequestConverter;
import org.aldeon.networking.conversion.RequestClassMapper;
import org.aldeon.networking.conversion.ResponseToByteBufferConverter;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.receiver.NettyRecvPoint;
import org.aldeon.networking.wrappers.RecvPointBasedReceiver;
import org.aldeon.protocol.Request;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.adapters.JsonModule;

public class NettyReceiverModule extends AbstractModule implements Provider<Receiver> {
    @Override
    protected void configure() {
        bind(JsonParser.class).toProvider(JsonModule.class);
    }

    @Override
    public Receiver get() {

        IpPeerAddress loopback = IpPeerAddress.create("0.0.0.0", 8080);
        RecvPoint point = new NettyRecvPoint(loopback);

        JsonParser parser = new JsonModule().get();
        ClassMapper<Request> mapper = new RequestClassMapper();

        Receiver receiver = new RecvPointBasedReceiver(point, new ByteBufferToRequestConverter(parser, mapper), new ResponseToByteBufferConverter(parser));

        return receiver;
    }
}

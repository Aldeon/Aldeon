package org.aldeon.networking.mediums.ip;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.common.Sender;
import org.aldeon.networking.conversion.ByteBufferToResponseConverter;
import org.aldeon.networking.conversion.RequestToByteBufferConverter;
import org.aldeon.networking.conversion.ResponseClassMapper;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.sender.NettySendPoint;
import org.aldeon.networking.wrappers.SendPointBasedSender;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.adapters.JsonModule;

import java.nio.ByteBuffer;
import java.util.Set;

public class NewNettySenderModule extends AbstractModule implements Provider<Sender> {

    @Override
    protected void configure() {

    }

    @Override
    public Sender get() {
        JsonParser parser = new JsonModule().get();
        ClassMapper<Response> mapper = new ResponseClassMapper();

        SendPoint point = new NettySendPoint();
        Set<AddressType> types = Sets.newHashSet(IpPeerAddress.IPV4, IpPeerAddress.IPV6);
        Converter<Request, ByteBuffer> encoder = new RequestToByteBufferConverter(parser);
        Converter<ByteBuffer, Response> decoder = new ByteBufferToResponseConverter(parser, mapper);


        return new SendPointBasedSender(point, types, encoder, decoder);
    }
}

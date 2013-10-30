package org.aldeon.communication.netty;

import com.google.inject.AbstractModule;
import org.aldeon.common.net.AddressTranslation;
import org.aldeon.common.net.address.IpPeerAddress;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.Sender;
import org.aldeon.communication.converter.JsonStringToRequestConverter;
import org.aldeon.communication.converter.JsonStringToResponseConverter;
import org.aldeon.communication.converter.RequestToJsonStringConverter;
import org.aldeon.communication.converter.ResponseToJsonStringConverter;
import org.aldeon.communication.netty.converter.*;
import org.aldeon.communication.netty.receiver.NettyReceiver;
import org.aldeon.communication.netty.sender.NettySender;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ChainConverter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.ConcreteJsonParser;
import org.aldeon.utils.json.JsonParser;


public class NettyModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    public static Sender<IpPeerAddress> createSender() {

        JsonParser parser = new ConcreteJsonParser();
        ClassMapper<Response> mapper = new ResponseClassMapper();

        return new NettySender(
            new ChainConverter<>(
                new RequestToJsonStringConverter(parser),
                new StringToFullHttpRequestConverter()
            ),
            new ChainConverter<>(
                    new FullHttpResponseToStringConverter(),
                    new JsonStringToResponseConverter(parser, mapper)
            )
        );
    }

    public static Receiver<IpPeerAddress> createReceiver(AddressTranslation translation) {

        JsonParser parser = new ConcreteJsonParser();
        ClassMapper<Request> mapper = new RequestClassMapper();

        return new NettyReceiver(
            translation,
            new ChainConverter<>(
                    new ResponseToJsonStringConverter(parser),
                    new StringToFullHttpResponseConverter()
            ),
            new ChainConverter<>(
                    new FullHttpRequestToStringConverter(),
                    new JsonStringToRequestConverter(parser, mapper)
            )
        );
    }
}

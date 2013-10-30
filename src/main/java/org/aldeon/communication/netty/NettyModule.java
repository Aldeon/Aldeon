package org.aldeon.communication.netty;

import com.google.inject.AbstractModule;
import org.aldeon.net.AddressTranslation;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.Sender;
import org.aldeon.communication.converter.*;
import org.aldeon.communication.netty.converter.*;
import org.aldeon.communication.netty.receiver.NettyReceiver;
import org.aldeon.communication.netty.sender.NettySender;
import org.aldeon.utils.conversion.ChainConverter;

/**
 * This class instantiates the Sender and Receiver classes implemented
 * using the Netty NIO framework.
 */
public class NettyModule extends AbstractModule {
    @Override
    protected void configure() {
        // We are probably going to declare JsonParser dependencies here in the near future.
    }

    public static Sender<IpPeerAddress> createSender() {

        return new NettySender(
            new ChainConverter<>(
                new RequestToJsonStringConverter(),
                new StringToFullHttpRequestConverter()
            ),
            new ChainConverter<>(
                    new FullHttpResponseToStringConverter(),
                    new JsonStringToResponseConverter()
            )
        );
    }

    public static Receiver<IpPeerAddress> createReceiver(AddressTranslation translation) {

        return new NettyReceiver(
            translation,
            new ChainConverter<>(
                    new ResponseToJsonStringConverter(),
                    new StringToFullHttpResponseConverter()
            ),
            new ChainConverter<>(
                    new FullHttpRequestToStringConverter(),
                    new JsonStringToRequestConverter()
            )
        );
    }
}

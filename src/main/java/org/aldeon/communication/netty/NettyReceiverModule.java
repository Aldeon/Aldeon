package org.aldeon.communication.netty;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.converter.JsonStringToRequestConverter;
import org.aldeon.communication.converter.ResponseToJsonStringConverter;
import org.aldeon.communication.netty.converter.FullHttpRequestToStringConverter;
import org.aldeon.communication.netty.converter.StringToFullHttpResponseConverter;
import org.aldeon.communication.netty.receiver.NettyReceiver;
import org.aldeon.nat.AddressTranslationModule;
import org.aldeon.net.AddressTranslation;
import org.aldeon.utils.conversion.ChainConverter;

public class NettyReceiverModule extends AbstractModule implements Provider<Receiver> {

    @Override
    protected void configure() {
        bind(AddressTranslation.class).toProvider(AddressTranslationModule.class);
    }

    @Override
    public Receiver get() {

        AddressTranslation translation = Guice.createInjector(this).getInstance(AddressTranslation.class);

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

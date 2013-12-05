package org.aldeon.communication.netty;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.converter.JsonStringToRequestConverter;
import org.aldeon.communication.converter.RequestClassMapper;
import org.aldeon.communication.converter.ResponseToJsonStringConverter;
import org.aldeon.communication.netty.converter.FullHttpRequestToStringConverter;
import org.aldeon.communication.netty.converter.StringToFullHttpResponseConverter;
import org.aldeon.communication.netty.receiver.NettyReceiver;
import org.aldeon.nat.AddressTranslationModule;
import org.aldeon.net.AddressTranslation;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ChainConverter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.adapters.JsonModule;

public class NettyReceiverModule extends AbstractModule implements Provider<Receiver> {

    @Override
    protected void configure() {
        bind(AddressTranslation.class).toProvider(AddressTranslationModule.class);
        bind(JsonParser.class).toProvider(JsonModule.class);
    }

    @Override
    public Receiver get() {

        Injector injector = Guice.createInjector(this);

        AddressTranslation translation = injector.getInstance(AddressTranslation.class);
        JsonParser parser = injector.getInstance(JsonParser.class);
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

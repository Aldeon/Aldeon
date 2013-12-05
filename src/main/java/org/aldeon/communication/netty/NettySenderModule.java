package org.aldeon.communication.netty;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import org.aldeon.communication.Sender;
import org.aldeon.communication.converter.JsonStringToResponseConverter;
import org.aldeon.communication.converter.RequestToJsonStringConverter;
import org.aldeon.communication.converter.ResponseClassMapper;
import org.aldeon.communication.netty.converter.FullHttpResponseToStringConverter;
import org.aldeon.communication.netty.converter.StringToFullHttpRequestConverter;
import org.aldeon.communication.netty.sender.NettySender;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ChainConverter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.adapters.JsonModule;

public class NettySenderModule extends AbstractModule implements Provider<Sender> {

    @Override
    protected void configure() {
        bind(JsonParser.class).toProvider(JsonModule.class);
    }

    @Override
    public Sender get() {

        JsonParser parser = Guice.createInjector(this).getInstance(JsonParser.class);
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
}

package org.aldeon.communication.netty;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import org.aldeon.communication.Sender;
import org.aldeon.communication.converter.JsonStringToResponseConverter;
import org.aldeon.communication.converter.RequestToJsonStringConverter;
import org.aldeon.communication.netty.converter.FullHttpResponseToStringConverter;
import org.aldeon.communication.netty.converter.StringToFullHttpRequestConverter;
import org.aldeon.communication.netty.sender.NettySender;
import org.aldeon.utils.conversion.ChainConverter;

public class NettySenderModule extends AbstractModule implements Provider<Sender> {

    @Override
    protected void configure() {

    }

    @Override
    public Sender get() {
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
}

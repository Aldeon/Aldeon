package org.aldeon.communication;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import org.aldeon.common.EndpointWithPortPolicy;
import org.aldeon.communication.http_provider.IPIdentifier;
import org.aldeon.communication.http_provider.JSONConverter;
import org.aldeon.communication.http_provider.java_sender.JavaSender;
import org.aldeon.communication.http_provider.jetty_receiver.JettyEndpointHandler;

public class CommunicationModule extends AbstractModule {
    @Override
    protected void configure() {
        //bind Abstract class to Concrete class

        bind(Sender.class).to(JavaSender.class);
        bind(Converter.class).to(JSONConverter.class);

        bind(new TypeLiteral<Receiver<String, IPIdentifier>>(){})
                .to(JettyEndpointHandler.class);
        bind(new TypeLiteral<Sender<String, IPIdentifier>>(){})
                .to(JavaSender.class);
        bind(new TypeLiteral<Converter<String>>(){})
                .to(JSONConverter.class);

        bind(ICommunicationProvider.class).to(
                new TypeLiteral<CommunicationProvider<String, IPIdentifier>>(){});

    }
    public static CommunicationProvider getProvider() {
        return null;
    }

    public static EndpointWithPortPolicy getEndpoint() {
        Injector jettyInjector = Guice.createInjector(new CommunicationModule());
        return jettyInjector.getInstance(EndpointWithPortPolicy.class);
    }
}

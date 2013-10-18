package org.aldeon.jetty;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import org.aldeon.common.EndpointWithConnectionPolicy;
import org.aldeon.jetty.handler.JettyEndpointHandler;
import org.aldeon.jetty.handler.ObserverAwareAbstractHandler;
import org.aldeon.jetty.json.ClassMapper;
import org.aldeon.jetty.json.ConcreteJsonParser;
import org.aldeon.jetty.json.JsonParser;
import org.aldeon.jetty.json.ProtocolClassMapper;
import org.aldeon.jetty.resolver.JsonQueryResolver;
import org.aldeon.jetty.resolver.QueryResolver;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.TestProtocol;
import org.aldeon.protocol.query.Query;

public class JettyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EndpointWithConnectionPolicy.class).to(JettyHttpEndpoint.class);
        bind(ObserverAwareAbstractHandler.class).to(JettyEndpointHandler.class);
        bind(Protocol.class).to(TestProtocol.class);
        bind(JsonParser.class).to(ConcreteJsonParser.class);
        bind(ClassMapper.class).to(ProtocolClassMapper.class);
        bind(QueryResolver.class).to(JsonQueryResolver.class);
        bind(new TypeLiteral<ClassMapper<Query>>() {
        }).to(ProtocolClassMapper.class);
    }

    public static EndpointWithConnectionPolicy getEndpoint() {
        Injector jettyInjector = Guice.createInjector(new JettyModule());
        return jettyInjector.getInstance(EndpointWithConnectionPolicy.class);
    }
}

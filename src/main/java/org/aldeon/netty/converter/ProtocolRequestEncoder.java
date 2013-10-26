package org.aldeon.netty.converter;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;


public class ProtocolRequestEncoder implements Converter<Request, FullHttpRequest> {
    @Override
    public FullHttpRequest convert(Request val) throws ConversionException {

        /*
         * Here we convert a protocol Request object into a HttpRequest.
         * This involves parsing into JSON, then into String (or byte[]).
         */


        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1,
                HttpMethod.GET,
                "/query"
        );

        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        return request;
    }
}

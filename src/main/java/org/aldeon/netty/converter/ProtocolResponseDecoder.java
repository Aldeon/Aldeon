package org.aldeon.netty.converter;

import io.netty.handler.codec.http.FullHttpResponse;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;

public class ProtocolResponseDecoder implements Converter<FullHttpResponse, Response> {
    @Override
    public Response convert(FullHttpResponse val) throws ConversionException {

        /*
         * Here we convert a HttpResponse into a protocol Response object.
         */

        return null;
    }
}

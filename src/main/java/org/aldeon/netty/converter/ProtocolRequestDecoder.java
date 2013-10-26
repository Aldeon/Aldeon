package org.aldeon.netty.converter;

import io.netty.handler.codec.http.FullHttpRequest;
import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;

public class ProtocolRequestDecoder implements Converter<FullHttpRequest, Request> {
    @Override
    public Request convert(FullHttpRequest val) throws ConversionException {

        /*
         * Here goes all the logic related to converting a HTTP request into a Request object.
         * JSON parser would be very useful here.
         */

        return null;
    }
}

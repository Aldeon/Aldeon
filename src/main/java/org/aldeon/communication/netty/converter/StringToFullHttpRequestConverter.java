package org.aldeon.communication.netty.converter;

import io.netty.handler.codec.http.*;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringToFullHttpRequestConverter implements Converter<String, FullHttpRequest> {
    @Override
    public FullHttpRequest convert(String val) throws ConversionException {
        try {
            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1,
                    HttpMethod.GET,
                    "/?query=" + URLEncoder.encode(val, "UTF-8")
            );
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            return request;
        } catch (UnsupportedEncodingException e) {
            // Should never happen
            throw new ConversionException();
        }
    }
}

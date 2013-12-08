package org.aldeon.networking.mediums.ip.conversion;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.helpers.ByteBuffers;

import java.net.URLEncoder;
import java.nio.ByteBuffer;

public class ByteBufferToFullHttpRequestConverter implements Converter<ByteBuffer, FullHttpRequest> {
    @Override
    public FullHttpRequest convert(ByteBuffer val) throws ConversionException {
        try {

            String req = new String(ByteBuffers.array(val));

            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1,
                    HttpMethod.GET,
                    "/?query=" + URLEncoder.encode(req, "UTF-8")
            );
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            return request;
        } catch (Exception e) {
            throw new ConversionException("Failed to convert given ByteBuffer into a HttpRequest.", e);
        }
    }
}

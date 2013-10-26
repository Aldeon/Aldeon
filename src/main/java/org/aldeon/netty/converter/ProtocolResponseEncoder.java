package org.aldeon.netty.converter;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;

public class ProtocolResponseEncoder implements Converter<Response, FullHttpResponse> {
    @Override
    public FullHttpResponse convert(Response val) throws ConversionException {

        /*
         * Here goes ann the logic related to converting a protocol Response object
         * into HttpResponse. For the time being, we just ignore tha argument and
         * create a placeholder so the web browser does not return garbage.
         */

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer("time: " + System.currentTimeMillis(), CharsetUtil.UTF_8)
        );

        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");

        return response;
    }
}

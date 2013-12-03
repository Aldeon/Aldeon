package org.aldeon.communication.netty.converter;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;

public class StringToFullHttpResponseConverter implements Converter<String, FullHttpResponse> {

    @Override
    public FullHttpResponse convert(String str) throws ConversionException {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(str, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/json; charset=UTF-8");
        return response;
    }
}

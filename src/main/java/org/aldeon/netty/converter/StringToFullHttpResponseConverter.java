package org.aldeon.netty.converter;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;

public class StringToFullHttpResponseConverter implements Converter<String, FullHttpResponse> {

    @Override
    public FullHttpResponse convert(String val) throws ConversionException {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(val, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        return response;
    }
}

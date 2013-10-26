package org.aldeon.netty.example_converters;

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
import org.aldeon.utils.json.ConcreteJsonParser;
import org.aldeon.utils.json.JsonParser;

public class ExampleResponseEncoder implements Converter<Response, FullHttpResponse> {

    private final JsonParser parser;

    public ExampleResponseEncoder() {
        parser = new ConcreteJsonParser();
    }

    @Override
    public FullHttpResponse convert(Response response) throws ConversionException {
        if(response == null)
            throw new ConversionException();

        return jsonResponse(parser.toJson(response));
    }

    private static FullHttpResponse jsonResponse(String json) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(json, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/json; charset=UTF-8");
        return response;
    }
}

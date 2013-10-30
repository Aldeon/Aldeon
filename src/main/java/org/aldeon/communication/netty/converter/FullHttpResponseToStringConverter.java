package org.aldeon.communication.netty.converter;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

public class FullHttpResponseToStringConverter implements Converter<FullHttpResponse, String> {

    private static final Logger log = LoggerFactory.getLogger(FullHttpResponseToStringConverter.class);

    @Override
    public String convert(FullHttpResponse response) throws ConversionException {

        ByteBuf buf = response.content();
        CharsetDecoder decoder = CharsetUtil.getDecoder(CharsetUtil.UTF_8);

        try {
            return decoder.decode(buf.nioBuffer()).toString();
        } catch (CharacterCodingException e) {
            throw new ConversionException("Failed to decode given ByteBuf to a CharBuffer using UTF8 decoder.", e);
        }
    }
}

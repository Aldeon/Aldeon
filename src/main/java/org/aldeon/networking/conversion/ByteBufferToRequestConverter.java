package org.aldeon.networking.conversion;

import com.google.inject.Inject;
import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.helpers.ByteBuffers;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.ParseException;

import java.nio.ByteBuffer;

public class ByteBufferToRequestConverter implements Converter<ByteBuffer, Request> {

    private final JsonParser parser;
    private final ClassMapper<Request> mapper;

    @Inject
    public ByteBufferToRequestConverter(JsonParser parser, ClassMapper<Request> mapper) {
        this.parser = parser;
        this.mapper = mapper;
    }

    @Override
    public Request convert(ByteBuffer jsonByteBuffer) throws ConversionException {
        try {
            byte[] raw = ByteBuffers.array(jsonByteBuffer);
            String json = new String(raw);
            return parser.fromJson(json, mapper);
        } catch (ParseException e) {
            throw new ConversionException("Failed to convert given String into a Request object", e);
        }
    }
}

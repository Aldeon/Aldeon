package org.aldeon.networking.conversion;

import com.google.inject.Inject;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.helpers.ByteBuffers;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.ParseException;

import java.nio.ByteBuffer;

public class ByteBufferToResponseConverter implements Converter<ByteBuffer, Response> {

    private final JsonParser parser;
    private final ClassMapper<Response> mapper;

    @Inject
    public ByteBufferToResponseConverter(JsonParser parser, ClassMapper<Response> mapper) {
        this.parser = parser;
        this.mapper = mapper;
    }

    @Override
    public Response convert(ByteBuffer buffer) throws ConversionException {
        try {
            return parser.fromJson(new String(ByteBuffers.array(buffer)), mapper);
        } catch (ParseException e) {
            throw new ConversionException("Failed to convert given ByteBuffer into a Response object", e);
        }
    }
}

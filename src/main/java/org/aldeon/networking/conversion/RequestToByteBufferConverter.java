package org.aldeon.networking.conversion;

import com.google.inject.Inject;
import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.JsonParser;

import java.nio.ByteBuffer;

public class RequestToByteBufferConverter implements Converter<Request, ByteBuffer> {

    private final JsonParser parser;

    @Inject
    public RequestToByteBufferConverter(JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public ByteBuffer convert(Request request) throws ConversionException {
        return ByteBuffer.wrap(parser.toJson(request).getBytes());
    }
}

package org.aldeon.networking.conversion;

import com.google.inject.Inject;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.JsonParser;

import java.nio.ByteBuffer;

public class ResponseToByteBufferConverter implements Converter<Response, ByteBuffer> {

    private final JsonParser parser;

    @Inject
    public ResponseToByteBufferConverter(JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public ByteBuffer convert(Response response) throws ConversionException {
        if(response == null) {
            throw new ConversionException("Cannot convert NULL into a Response");
        } else {
            try {
                return ByteBuffer.wrap(parser.toJson(response).getBytes());
            } catch (Exception e) {
                throw new ConversionException("Failed to convert Response into ByteBuffer", e);
            }
        }
    }
}

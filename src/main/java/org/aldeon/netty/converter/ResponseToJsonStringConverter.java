package org.aldeon.netty.converter;

import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.JsonParser;

public class ResponseToJsonStringConverter implements Converter<Response, String> {

    private final JsonParser parser;

    public ResponseToJsonStringConverter(JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public String convert(Response val) throws ConversionException {
        return parser.toJson(val);
    }
}

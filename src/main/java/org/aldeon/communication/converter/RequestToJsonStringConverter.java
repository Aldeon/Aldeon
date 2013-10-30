package org.aldeon.communication.converter;

import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.JsonParser;

public class RequestToJsonStringConverter implements Converter<Request, String> {

    private final JsonParser parser;

    public RequestToJsonStringConverter(JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public String convert(Request request) throws ConversionException {
        return parser.toJson(request);
    }
}

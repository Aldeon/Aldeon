package org.aldeon.communication.converter;

import org.aldeon.common.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.JsonParserImpl;
import org.aldeon.utils.json.JsonParser;

public class RequestToJsonStringConverter implements Converter<Request, String> {

    private final JsonParser parser;

    public RequestToJsonStringConverter() {
        this.parser = new JsonParserImpl();
    }

    @Override
    public String convert(Request request) throws ConversionException {
        return parser.toJson(request);
    }
}

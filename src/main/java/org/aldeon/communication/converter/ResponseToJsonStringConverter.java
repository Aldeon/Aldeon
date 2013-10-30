package org.aldeon.communication.converter;

import org.aldeon.common.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.JsonParserImpl;
import org.aldeon.utils.json.JsonParser;

public class ResponseToJsonStringConverter implements Converter<Response, String> {

    private final JsonParser parser;

    public ResponseToJsonStringConverter() {
        this.parser = new JsonParserImpl();
    }

    @Override
    public String convert(Response val) throws ConversionException {
        return parser.toJson(val);
    }
}

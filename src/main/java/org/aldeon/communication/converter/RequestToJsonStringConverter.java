package org.aldeon.communication.converter;

import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.JsonParserImpl;

/**
 * This class is part of a conversion chain used when encoding an outbound
 * request. Here we convert a Request into its string representation.
 */
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

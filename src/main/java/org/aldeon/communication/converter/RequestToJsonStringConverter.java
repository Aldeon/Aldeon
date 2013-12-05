package org.aldeon.communication.converter;

import com.google.inject.Inject;
import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.GsonBasedJsonParser;
import org.aldeon.utils.json.JsonParser;

/**
 * This class is part of a conversion chain used when encoding an outbound
 * request. Here we convert a Request into its string representation.
 */
public class RequestToJsonStringConverter implements Converter<Request, String> {

    private final JsonParser parser;

    @Inject
    public RequestToJsonStringConverter(JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public String convert(Request request) throws ConversionException {
        return parser.toJson(request);
    }
}

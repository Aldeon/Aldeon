package org.aldeon.communication.converter;

import com.google.inject.Inject;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.JsonParser;

/**
 * This class is used as part of a conversion chain used when encoding a response
 * to send it back to a peer.
 */
public class ResponseToJsonStringConverter implements Converter<Response, String> {

    private final JsonParser parser;

    @Inject
    public ResponseToJsonStringConverter(JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public String convert(Response response) throws ConversionException {
        if(response == null) {
            throw new ConversionException("Cannot convert NULL into a Response");
        } else {
            try {
                return parser.toJson(response);
            } catch (Exception e) {
                throw new ConversionException("Failed to convert Response into string", e);
            }
        }
    }
}

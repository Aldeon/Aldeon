package org.aldeon.communication.converter;

import com.google.inject.Inject;
import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.ParseException;

/**
 * This class is part of a conversion chain used when decoding
 * an incoming request. Here we convert a string representation
 * into a proper Request instance.
 */
public class JsonStringToRequestConverter implements Converter<String, Request> {

    private final JsonParser parser;
    private final ClassMapper<Request> mapper;

    @Inject
    public JsonStringToRequestConverter(JsonParser parser, ClassMapper<Request> mapper) {
        this.parser = parser;
        this.mapper = mapper;
    }

    @Override
    public Request convert(String json) throws ConversionException {
        try {
            return parser.fromJson(json, mapper);
        } catch (ParseException e) {
            throw new ConversionException("Failed to convert given String into a Request object", e);
        }
    }
}

package org.aldeon.communication.converter;

import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParserImpl;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.ParseException;

/**
 * This class is part of a conversion chain used when decoding an incoming response
 * from other server.
 */
public class JsonStringToResponseConverter implements Converter<String, Response> {

    private final JsonParser parser;
    private final ClassMapper<Response> mapper;

    public JsonStringToResponseConverter() {
        this.parser = new JsonParserImpl();
        this.mapper = new ResponseClassMapper();
    }

    @Override
    public Response convert(String json) throws ConversionException {
        try {
            return parser.fromJson(json, mapper);
        } catch (ParseException e) {
            throw new ConversionException("Failed to convert given String into a Response object", e);
        }
    }
}

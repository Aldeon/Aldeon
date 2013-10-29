package org.aldeon.netty.converter;

import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.ParseException;

public class JsonStringToRequestConverter implements Converter<String, Request> {

    private final JsonParser parser;
    private final ClassMapper<Request> mapper;

    public JsonStringToRequestConverter(JsonParser parser, ClassMapper<Request> mapper) {
        this.parser = parser;
        this.mapper = mapper;
    }

    @Override
    public Request convert(String val) throws ConversionException {
        try {
            return parser.fromJson(val, mapper);
        } catch (ParseException e) {
            throw new ConversionException();
        }
    }
}

package org.aldeon.communication.converter;

import org.aldeon.common.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.JsonParserImpl;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.ParseException;

public class JsonStringToRequestConverter implements Converter<String, Request> {

    private final JsonParser parser;
    private final ClassMapper<Request> mapper;

    public JsonStringToRequestConverter() {
        this.parser = new JsonParserImpl();
        this.mapper = new RequestClassMapper();
    }

    @Override
    public Request convert(String json) throws ConversionException {
        try {
            return parser.fromJson(json, mapper);
        } catch (ParseException e) {
            throw new ConversionException();
        }
    }
}

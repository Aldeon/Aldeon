package org.aldeon.netty.example_converters;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.aldeon.protocol.Request;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.json.ClassMapper;
import org.aldeon.utils.json.ConcreteJsonParser;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.json.ParseException;

import java.util.List;
import java.util.Map;

public class ExampleRequestDecoder implements Converter<FullHttpRequest, Request> {

    private final JsonParser parser;
    private final ClassMapper<Request> mapper;

    public ExampleRequestDecoder() {
        parser = new ConcreteJsonParser();
        mapper = new ExampleClassMapper();
    }

    @Override
    public Request convert(FullHttpRequest request) throws ConversionException {

        // Fetch the 'query' param
        // example:     http://123.123.123.123:8080/?query={"action":"date"}
        String req = getRequestParam(request, "query");

        if(req != null) {
            try {
                return parser.fromJson(req, mapper);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        throw new ConversionException();
    }

    private static String getRequestParam(FullHttpRequest request, String key) {
        Map<String, List<String>> params = new QueryStringDecoder(request.getUri()).parameters();
        if(params.containsKey(key)){
            List<String> plist = params.get(key);
            if(plist.size() == 1) {
                return plist.get(0);
            }
        }
        return null;
    }
}

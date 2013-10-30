package org.aldeon.communication.netty.converter;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;

import java.util.List;
import java.util.Map;

public class FullHttpRequestToStringConverter implements Converter<FullHttpRequest, String> {

    @Override
    public String convert(FullHttpRequest val) throws ConversionException {
        String result = getRequestParam(val, "query");
        if(result == null) {
            throw new ConversionException("Could convert a FullHttpRequest into a String because " +
                    "the request does not contain a GET field named 'query'.");
        } else {
            return result;
        }
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

package org.aldeon.jetty.json;

public interface JsonParser {
    public <T> T fromJson(String json, Class<T> classOfT) throws ParseException;
    public<T> T fromJson(String json, ClassMapper<T> mapper) throws ParseException;
    public String toJson(Object object);
}

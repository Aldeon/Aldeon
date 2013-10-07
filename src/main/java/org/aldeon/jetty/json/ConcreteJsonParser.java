package org.aldeon.jetty.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConcreteJsonParser implements JsonParser {

    private Gson gson;
    private com.google.gson.JsonParser parser;

    public ConcreteJsonParser() {
        gson = new Gson();
        parser = new com.google.gson.JsonParser();
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(parse(json), classOfT);
    }

    @Override
    public <T> T fromJson(String json, ClassMapper<T> mapper) throws ParseException{
        JsonObject jsonObject = parse(json);
        Class<? extends T> classOfT = mapper.getClass(jsonObject);
        if(classOfT == null) {
            throw new ParseException();
        } else {
            return gson.fromJson(jsonObject, classOfT);
        }
    }

    @Override
    public String toJson(Object object) {
        return gson.toJson(object);
    }

    private JsonObject parse(String string) {
        return (JsonObject) parser.parse(string);
    }
}

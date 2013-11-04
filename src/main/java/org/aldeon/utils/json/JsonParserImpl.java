package org.aldeon.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.aldeon.crypt.Signature;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.utils.base64.Base64Codec;
import org.aldeon.utils.base64.Base64CodecImpl;
import org.aldeon.utils.json.adapters.IdentifierDeserializer;
import org.aldeon.utils.json.adapters.IdentifierSerializer;
import org.aldeon.utils.json.adapters.MessageSerializer;
import org.aldeon.utils.json.adapters.SignatureSerializer;

public class JsonParserImpl implements JsonParser {

    private Gson gson;
    private com.google.gson.JsonParser parser;

    public JsonParserImpl() {

        GsonBuilder builder = new GsonBuilder();

        Base64Codec base64 = new Base64CodecImpl();

        /*
            Here we register serializers and deserializers for all important components
            of Requests and responses, for example:
                - Identifiers
                - Signatures
                - Public keys
                - Addresses
                - Messages
         */

        builder.registerTypeAdapter(Identifier.class, new IdentifierSerializer(base64));
        builder.registerTypeAdapter(Identifier.class, new IdentifierDeserializer(base64));
        builder.registerTypeAdapter(Signature.class, new SignatureSerializer(base64));
        builder.registerTypeAdapter(Message.class, new MessageSerializer());

        gson = builder.create();
        parser = new com.google.gson.JsonParser();
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) throws ParseException {
        try {
            return gson.fromJson(parse(json), classOfT);
        } catch(Exception e) {
            throw new ParseException("Failed to parse the given string", e);
        }
    }

    @Override
    public <T> T fromJson(String json, ClassMapper<T> mapper) throws ParseException {
        JsonObject jsonObject = parse(json);
        Class<? extends T> classOfT = mapper.getClass(jsonObject);
        if(classOfT == null) {
            throw new ParseException("Could not map JSON object to appropriate class");
        } else {
            try {
                return gson.fromJson(parse(json), classOfT);
            } catch(Exception e) {
                throw new ParseException("Failed to parse the given string", e);
            }
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
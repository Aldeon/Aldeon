package org.aldeon.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import org.aldeon.crypt.Key;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.model.Signature;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.base64.Base64;
import org.aldeon.utils.json.adapters.IdentifierDeserializer;
import org.aldeon.utils.json.adapters.IdentifierSerializer;
import org.aldeon.utils.json.adapters.Ipv4PeerAddressDeserializer;
import org.aldeon.utils.json.adapters.Ipv4PeerAddressSerializer;
import org.aldeon.utils.json.adapters.KeySerializer;
import org.aldeon.utils.json.adapters.MessageSerializer;
import org.aldeon.utils.json.adapters.PeerAddressDeserializer;
import org.aldeon.utils.json.adapters.SignatureSerializer;

public class GsonBasedJsonParser implements JsonParser {

    private Gson gson;
    private com.google.gson.JsonParser parser;

    @Inject
    public GsonBasedJsonParser(Base64 base64) {

        GsonBuilder builder = new GsonBuilder();

        /*
            Here we register serializers and deserializers for all important components
            of Requests and responses
         */

        builder.registerTypeAdapter(Identifier.class, new IdentifierSerializer(base64));
        builder.registerTypeAdapter(Identifier.class, new IdentifierDeserializer(base64));

        builder.registerTypeAdapter(Key.class, new KeySerializer(base64));

        builder.registerTypeAdapter(Signature.class, new SignatureSerializer(base64));
        builder.registerTypeAdapter(Message.class, new MessageSerializer());

        builder.registerTypeAdapter(Ipv4PeerAddress.class, new Ipv4PeerAddressSerializer());
        builder.registerTypeAdapter(Ipv4PeerAddress.class, new Ipv4PeerAddressDeserializer());

        builder.registerTypeAdapter(PeerAddress.class, new PeerAddressDeserializer());

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

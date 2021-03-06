package org.aldeon.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import org.aldeon.crypt.Key;
import org.aldeon.crypt.KeyGen;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.model.Signature;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.json.adapters.IdentifierDeserializer;
import org.aldeon.utils.json.adapters.IdentifierSerializer;
import org.aldeon.utils.json.adapters.KeyDeserializer;
import org.aldeon.utils.json.adapters.KeySerializer;
import org.aldeon.utils.json.adapters.MessageDeserializer;
import org.aldeon.utils.json.adapters.MessageSerializer;
import org.aldeon.utils.json.adapters.PeerAddressDeserializer;
import org.aldeon.utils.json.adapters.PeerAddressSerializer;
import org.aldeon.utils.json.adapters.SignatureDeserializer;
import org.aldeon.utils.json.adapters.SignatureSerializer;

public class GsonBasedJsonParser implements JsonParser {

    private Gson gson;
    private com.google.gson.JsonParser parser;

    @Inject
    public GsonBasedJsonParser(Codec codec, KeyGen keyGen) {

        GsonBuilder builder = new GsonBuilder();

        /*
            Here we register serializers and deserializers for all important components
            of Requests and responses
         */

        builder.registerTypeAdapter(Identifier.class, new IdentifierSerializer(codec));
        builder.registerTypeAdapter(Identifier.class, new IdentifierDeserializer(codec));

        builder.registerTypeAdapter(Key.class, new KeySerializer(codec));
        builder.registerTypeAdapter(Key.class, new KeyDeserializer(codec, keyGen));

        builder.registerTypeAdapter(Signature.class, new SignatureSerializer(codec));
        builder.registerTypeAdapter(Signature.class, new SignatureDeserializer(codec));

        builder.registerTypeAdapter(Message.class, new MessageSerializer());
        builder.registerTypeAdapter(Message.class, new MessageDeserializer());

        builder.registerTypeAdapter(PeerAddress.class, new PeerAddressSerializer());
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

    private JsonObject parse(String string) throws ParseException {
        try {
        return (JsonObject) parser.parse(string);
        } catch (Exception e) {
            throw new ParseException("Could not parse the input", e);
        }
    }
}

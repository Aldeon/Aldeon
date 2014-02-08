package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.crypt.Key;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.exception.KeyParseException;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class KeyDeserializer implements JsonDeserializer<Key> {
    private final Codec codec;
    private final KeyGen keygen;

    public KeyDeserializer(Codec codec, KeyGen keyGen) {
        this.codec = codec;
        this.keygen = keyGen;
    }

    @Override
    public Key deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ByteBuffer result;

        try {
            result = codec.decode(json.getAsString());
        } catch (ConversionException e) {
            throw new JsonParseException("Invalid base64 representation of binary data", e);
        }

        try {
            return keygen.parsePublicKey(result);
        } catch (KeyParseException e) {
            throw new JsonParseException("Failed to parse the public key", e);
        }
    }
}

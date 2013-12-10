package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.model.Signature;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class SignatureDeserializer implements JsonDeserializer<Signature> {

    private final Codec codec;

    public SignatureDeserializer(Codec codec) {
        this.codec = codec;
    }

    @Override
    public Signature deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ByteBuffer result;

        try {
            result = codec.decode(json.getAsString());
        } catch (ConversionException e) {
            throw new JsonParseException("Invalid base64 representation of binary data", e);
        }

        try {
            return new Signature(result, false);
        } catch(IllegalArgumentException e) {
            throw new JsonParseException("String representation of Signature has invalid size (detected "
                    + result.capacity() + " bytes, should be " + Signature.LENGTH_BYTES + ")");
        }
    }
}

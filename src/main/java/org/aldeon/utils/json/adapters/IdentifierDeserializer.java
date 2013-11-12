package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.model.Id;
import org.aldeon.model.Identifier;
import org.aldeon.utils.base64.Base64Codec;
import org.aldeon.utils.conversion.ConversionException;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class IdentifierDeserializer implements JsonDeserializer<Identifier> {

    private final Base64Codec base64;

    public IdentifierDeserializer(Base64Codec base64) {
        this.base64 = base64;
    }

    @Override
    public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ByteBuffer result;

        try {
            result = base64.decode(json.getAsString());
        } catch (ConversionException e) {
            throw new JsonParseException("JSON representation of Identifier is invalid", e);
        }

        try {
            return Id.fromByteBuffer(result, false);
        } catch(IllegalArgumentException e) {
            throw new JsonParseException("Base64 representation of Identifier has invalid size (detected "
                    + result.capacity() + " bytes, should be " + Identifier.LENGTH_BYTES + ")");
        }
    }
}

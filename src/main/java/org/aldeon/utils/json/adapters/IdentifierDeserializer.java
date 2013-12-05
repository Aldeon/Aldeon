package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.model.Identifier;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class IdentifierDeserializer implements JsonDeserializer<Identifier> {

    private final Codec codec;

    public IdentifierDeserializer(Codec codec) {
        this.codec = codec;
    }

    @Override
    public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ByteBuffer result;

        try {
            result = codec.decode(json.getAsString());
        } catch (ConversionException e) {
            throw new JsonParseException("JSON representation of Identifier is invalid", e);
        }

        try {
            return Identifier.fromByteBuffer(result, false);
        } catch(IllegalArgumentException e) {
            throw new JsonParseException("String representation of Identifier has invalid size (detected "
                    + result.capacity() + " bytes, should be " + Identifier.LENGTH_BYTES + ")");
        }
    }
}

package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.model.Identifier;
import org.aldeon.utils.base64.Base64Codec;

import java.lang.reflect.Type;

public class IdentifierSerializer implements JsonSerializer<Identifier> {

    private final Base64Codec base64;

    public IdentifierSerializer(Base64Codec base64) {
        this.base64 = base64;
    }

    @Override
    public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(base64.encode(src.getByteBuffer()));
    }
}

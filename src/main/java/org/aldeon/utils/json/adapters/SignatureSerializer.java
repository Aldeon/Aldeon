package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.crypt.Signature;
import org.aldeon.utils.base64.Base64Codec;

import java.lang.reflect.Type;

public class SignatureSerializer implements JsonSerializer<Signature> {
    private final Base64Codec base64;

    public SignatureSerializer(Base64Codec base64) {
        this.base64 = base64;
    }

    @Override
    public JsonElement serialize(Signature src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(base64.encode(src.getByteBuffer()));
    }
}

package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.crypt.Signature;
import org.aldeon.utils.base64.Base64Codec;

import java.lang.reflect.Type;

public class SignatureSerializer extends ByteSourceSerializer<Signature> {
    public SignatureSerializer(Base64Codec base64) {
        super(base64);
    }
}

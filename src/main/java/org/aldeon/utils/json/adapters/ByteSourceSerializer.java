package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.model.ByteSource;
import org.aldeon.utils.codec.Codec;

import java.lang.reflect.Type;

public class ByteSourceSerializer<T extends ByteSource> implements JsonSerializer<T> {

    private final Codec codec;

    public ByteSourceSerializer(Codec codec) {
        this.codec = codec;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(codec.encode(src.getByteBuffer()));
    }
}

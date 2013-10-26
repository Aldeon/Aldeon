package org.aldeon.utils.json;

import com.google.gson.JsonObject;

public interface ClassMapper<T> {
    public Class<? extends T> getClass(JsonObject jsonObject);
}

package org.aldeon.utils.json;

import com.google.gson.JsonObject;

/**
 * Maps the jsonObject structure to a class.
 * @param <T>
 */
public interface ClassMapper<T> {
    /**
     * Determines tha class based on a JsonObject structure
     * @param jsonObject json object
     * @return
     */
    public Class<? extends T> getClass(JsonObject jsonObject);
}

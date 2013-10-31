package org.aldeon.utils.json;

/**
 * Wrapper used to serialize/deserialize Java objects into JSON
 */
public interface JsonParser {

    /**
     * Creates an object instance.
     * @param json string holding a JSON representation
     * @param classOfT class of the result object
     * @param <T>
     * @return
     * @throws ParseException
     */
    public <T> T fromJson(String json, Class<T> classOfT) throws ParseException;

    /**
     * Creates an object instance.
     * @param json string holding a JSON representation
     * @param mapper finds the class of the result object based on the JSON structure
     * @param <T>
     * @return
     * @throws ParseException
     */
    public<T> T fromJson(String json, ClassMapper<T> mapper) throws ParseException;

    /**
     * Creates a JSON representation of an object.
     * @param object analysed object
     * @return
     */
    public String toJson(Object object);
}

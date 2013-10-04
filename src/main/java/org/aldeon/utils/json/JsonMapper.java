package org.aldeon.utils.json;


public interface JsonMapper<T> {

    public String toJson(T object);
    public T fromJson(String json) throws IllegalArgumentException;

}

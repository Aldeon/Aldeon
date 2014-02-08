package org.aldeon.utils.json;

/**
 * Maps an object to a class.
 * @param <T>
 */
public interface ClassMapper<T> {
    /**
     * Determines the class based on a given object
     * @param object object
     * @return
     */
    public Class<? extends T> getClass(Object object);
}

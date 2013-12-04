package org.aldeon.utils.collections;

public interface DependencyDispatcher<T> {

    /**
     * Returns a previously not returned orphan value
     * @return
     */
    T next();

    /**
     * Removes the value and unlocks the values that depend on it.
     * @param element
     */
    void remove(T element);

    /**
     * Indicates if all elements have been removed
     * @return
     */
    boolean isFinished();

}

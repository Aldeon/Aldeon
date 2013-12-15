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
     * Removes the element and all its descendants. The returned values
     * will never be returned using the next() function.
     * @param element
     */
    void removeRecursively(T element);

    /**
     * Indicates if all elements have been removed
     * @return
     */
    boolean isFinished();

}

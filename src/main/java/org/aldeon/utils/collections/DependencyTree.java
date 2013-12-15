package org.aldeon.utils.collections;

import java.util.Set;

public interface DependencyTree<T> {

    /**
     * Puts the element into a tree.
     *
     * Only one element is inserted this way - the parent does not count as inserted.
     * @param element element
     * @param parent  the parent (dependency) of given element
     */
    void insert(T element, T parent);

    /**
     * Removes the element and all relations from collection
     * @param element element to be removed
     */
    void remove(T element);

    /**
     * Removes the element and all its descendants.
     * @param element
     */
    void removeRecursively(T element);

    /**
     * Checks if the collection contains no elements
     * @return true if no elements are stored in the collection
     */
    boolean isEmpty();

    /**
     * Checks if the element exists in the collection
     * @param element element to look for
     * @return  true if element exists in the collection
     */
    boolean contains(T element);

    /**
     * Returns a set of all elements whose parents are not stored in the collection.
     * @return set
     */
    Set<T> getOrphans();
}

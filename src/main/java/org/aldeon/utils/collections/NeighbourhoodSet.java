package org.aldeon.utils.collections;

import java.util.Set;

public interface NeighbourhoodSet<E> extends Set<E> {
    public Set<E> closestValues(E value, int resultCount);
}

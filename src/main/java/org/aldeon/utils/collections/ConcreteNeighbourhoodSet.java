package org.aldeon.utils.collections;

import java.util.Set;

/**
 * Required for DHT implementation
 * @param <E>
 */
public class ConcreteNeighbourhoodSet<E> extends CircularTreeSet<E> implements NeighbourhoodSet<E>{

    @Override
    public Set<E> closestValues(E value, int resultCount) {
        //TODO: implement
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}

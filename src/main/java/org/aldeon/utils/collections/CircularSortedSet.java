package org.aldeon.utils.collections;

import java.util.Iterator;
import java.util.SortedSet;

public interface CircularSortedSet<E> extends SortedSet<E> {
    Iterator<E> fwdIterator(E start);
    Iterator<E> revIterator(E start);
}

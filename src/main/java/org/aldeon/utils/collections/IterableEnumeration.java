package org.aldeon.utils.collections;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IterableEnumeration<T> implements Iterable<T> {

    private List<T> list = new LinkedList<>();

    public IterableEnumeration(Enumeration<T> enumeration) {
        while(enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}

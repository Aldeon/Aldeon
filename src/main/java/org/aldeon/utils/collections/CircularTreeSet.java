package org.aldeon.utils.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class CircularTreeSet<E> extends TreeSet<E> implements CircularSortedSet<E> {

    public CircularTreeSet() {
        super();
    }

    public CircularTreeSet(Comparator<? super E> comparator) {
        super(comparator);
    }

    @Override
    public Iterator<E> fwdIterator(E start) {
        CircularIterator<E> ret = new CircularIterator<E>() {
            @Override
            public Iterator<E> onEmptyIterator() {
                return CircularTreeSet.this.iterator();
            }
        };
        ret.it = tailSet(start, true).iterator();
        return ret;
    }

    @Override
    public Iterator<E> revIterator(E start) {
        CircularIterator<E> ret = new CircularIterator<E>() {
            @Override
            public Iterator<E> onEmptyIterator() {
                return CircularTreeSet.this.descendingIterator();
            }
        };
        ret.it = headSet(start, true).descendingIterator();
        return ret;
    }

    private abstract class CircularIterator<E> implements Iterator<E> {
        protected Iterator<E> it;

        @Override
        public boolean hasNext() {
            return size() > 0;
        }

        @Override
        public void remove() {
            it.remove();
        }

        @Override
        public E next() {
            if(!it.hasNext()) {
                it = onEmptyIterator();
            }
            return it.next();
        }

        public abstract Iterator<E> onEmptyIterator();
    }
}

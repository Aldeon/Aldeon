package org.aldeon.utils.collections;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CircularTreeSetTest {

    @Test
    public void shouldIterate() {

        CircularTreeSet<Integer> set = new CircularTreeSet<>();

        set.add(5);
        set.add(3);
        set.add(7);
        set.add(4);

        Iterator<Integer> it = set.iterator();

        assertEquals(3, (int) it.next());
        assertEquals(4, (int) it.next());
        assertEquals(5, (int) it.next());
        assertEquals(7, (int) it.next());
        assertFalse(it.hasNext());
    }


    @Test
    public void shouldIterateForward() {

        CircularTreeSet<Integer> set = new CircularTreeSet<>();

        set.add(5);
        set.add(3);
        set.add(7);
        set.add(4);

        Iterator<Integer> it = set.fwdIterator(6);

        assertEquals(7, (int) it.next());
        assertEquals(3, (int) it.next());
        assertEquals(4, (int) it.next());
        assertEquals(5, (int) it.next());
        assertEquals(7, (int) it.next());
        assertEquals(3, (int) it.next());
        assertTrue(it.hasNext());
    }

    @Test
    public void shouldIterateBackward() {

        CircularTreeSet<Integer> set = new CircularTreeSet<>();

        set.add(5);
        set.add(1);
        set.add(2);
        set.add(8);

        Iterator<Integer> it = set.revIterator(5);

        assertEquals(5, (int) it.next());
        assertEquals(2, (int) it.next());
        assertEquals(1, (int) it.next());
        assertEquals(8, (int) it.next());
        assertEquals(5, (int) it.next());
        assertEquals(2, (int) it.next());
        assertTrue(it.hasNext());
    }

    @Test
    public void shouldNotIterateForwardForEmptySet() {
        CircularTreeSet<Integer> set = new CircularTreeSet<>();
        Iterator<Integer> it = set.fwdIterator(0);
        assertFalse(it.hasNext());
    }

    @Test
    public void shouldNotIterateBackwardForEmptySet() {
        CircularTreeSet<Integer> set = new CircularTreeSet<>();
        Iterator<Integer> it = set.revIterator(0);
        assertFalse(it.hasNext());
    }
}

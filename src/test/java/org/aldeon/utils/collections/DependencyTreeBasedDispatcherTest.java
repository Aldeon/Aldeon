package org.aldeon.utils.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DependencyTreeBasedDispatcherTest {

    @Test
    public void shouldAvoidDuplicateReturns() {

        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 5);
        tree.insert(5, 7);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        assertEquals(5, (int) d.next());    // orphan
        assertEquals(null, d.next());       // null - avoid duplicates

    }

    @Test
    public void shouldResumeWhenParentRemoved() {

        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 5);
        tree.insert(5, 7);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        assertEquals(5, (int) d.next());    // orphan

        d.remove(5);

        assertEquals(1, (int) d.next());
    }

    @Test
    public void shouldUpdateAccordinglyIfElementIsRemovedBeforeReturned() {

        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 2);
        tree.insert(2, 3);
        tree.insert(3, 4);
        tree.insert(4, 5);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        d.remove(4);

        assertEquals(3, (int) d.next());
        assertEquals(null, d.next());

        d.remove(2);

        assertEquals(1, (int) d.next());
    }

    @Test
    public void shouldIndicateEndOnFreshCollection() {

        DependencyTree<Integer> tree = new HashDependencyTree<>();
        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        assertTrue(d.isFinished());
    }

    @Test
    public void shouldNotIndicteEndIfCollectionNotEmpty() {

        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 2);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        assertFalse(d.isFinished());
    }

    @Test
    public void shouldNotIndicateEndIfElementWasReturnedAndNotRemoved() {

        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 2);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        d.next();   // returned 1

        assertFalse(d.isFinished());
    }

    @Test
    public void shouldIndicateEndIfElementWasReturnedAndRemoved() {

        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 2);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        d.next();   // returned 1

        d.remove(1);

        assertTrue(d.isFinished());
    }

    @Test
    public void shouldIndicateEndIfElementWasRemovedBeforeBeingReturned() {

        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 2);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        d.remove(1);

        assertTrue(d.isFinished());
    }

    @Test
    public void shouldFinishImmediatelyIfRemovingRecursivelyAllElements() {
        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 2);
        tree.insert(2, 3);
        tree.insert(3, 4);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        d.removeRecursively(3);

        assertTrue(d.isFinished());
    }

    @Test
    public void shouldNotRemoveRecursivelyUnrelatedElements() {
        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 2);
        tree.insert(2, 3);
        tree.insert(3, 4);
        tree.insert(7, 8);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        d.removeRecursively(3);

        assertEquals(7, (int) d.next());
        assertEquals(null, d.next());
        assertFalse(d.isFinished());
    }

    @Test
    public void shouldUpdateOrphansOnRecursiveRemoval() {
        DependencyTree<Integer> tree = new HashDependencyTree<>();

        tree.insert(1, 2);
        tree.insert(2, 3);

        tree.insert(4, 5);
        tree.insert(5, 6);

        DependencyTreeBasedDispatcher<Integer> d = new DependencyTreeBasedDispatcher<>(tree);

        Integer next = d.next();
        assertNotEquals(null, next);

        if(next == 2) {

            d.removeRecursively(5);
            assertEquals(null, d.next());

        } else { //next == 5

            d.removeRecursively(2);
            assertEquals(null, d.next());
        }

    }


}

package org.aldeon.utils.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HashDependencyTreeTest {

    @Test
    public void shouldReturnTrueForContainsInsertedElement() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 5);

        assertTrue(dt.contains(1));
    }

    @Test
    public void shouldReturnFalseForContainsParent() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 5);

        assertFalse(dt.contains(5));
    }

    @Test
    public void shouldDetectElementAsOrphan() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 5);
        dt.insert(5, 8);

        assertTrue(dt.getOrphans().contains(5));
    }

    @Test
    public void shouldNotDetectAsOrphanWhenParentIsPresent() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 3);
        dt.insert(3, 6);

        assertFalse(dt.getOrphans().contains(1));
    }

    @Test
    public void shouldDetectLoop() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 2);
        dt.insert(2, 3);
        dt.insert(3, 1);

        assertTrue(dt.getOrphans().isEmpty());
    }

    @Test
    public void shouldUpdateOrphansWhenParentInserted() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 3);

        assertTrue(dt.getOrphans().contains(1));

        dt.insert(3, 5);

        assertFalse(dt.getOrphans().contains(1));
    }

    @Test
    public void shouldUpdateOrphansWhenParentRemoved() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(2, 5);
        dt.insert(5, 7);

        assertFalse(dt.getOrphans().contains(2));

        dt.remove(5);

        assertTrue(dt.getOrphans().contains(2));
    }

    @Test
    public void shouldNotReturnOrphanElementIfRemoved(){
        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 2);
        dt.insert(2, 3);
        dt.insert(3, 4);

        dt.remove(3);

        assertEquals(1, dt.getOrphans().size());
        assertTrue(dt.getOrphans().contains(2));
    }

    @Test
    public void shouldBeEmptyIfNoElementsInserted() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        assertTrue(dt.isEmpty());
    }

    @Test
    public void shouldNotBeEmptyIfElementWasInserted() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 2);

        assertFalse(dt.isEmpty());
    }

    @Test
    public void shouldBecomeEmptyWhenLastElementIsRemoved() {

        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 2);

        dt.remove(1);

        assertTrue(dt.isEmpty());
    }

    @Test
    public void shouldRemoveParentWhenRemovedRecursively() {
        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 2);
        dt.insert(2, 3);

        assertTrue(dt.contains(2));
        dt.removeRecursively(2);
        assertFalse(dt.contains(2));
    }

    @Test
    public void shouldRemoveChildWhenRemovedRecursively() {
        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 2);
        dt.insert(2, 3);

        assertTrue(dt.contains(1));
        dt.removeRecursively(2);
        assertFalse(dt.contains(1));
    }

    @Test
    public void shouldBecomeEmptyIfAllRemovedRecursively() {
        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 2);
        dt.insert(2, 3);
        dt.insert(3, 4);

        assertFalse(dt.isEmpty());
        dt.removeRecursively(3);
        assertTrue(dt.isEmpty());
    }

    @Test
    public void shouldNotRemoveUnrelatedElements() {
        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(5, 6);

        dt.insert(1, 2);
        dt.insert(2, 3);

        dt.removeRecursively(2);
        assertEquals(1, dt.getOrphans().size());
        assertTrue(dt.getOrphans().contains(5));
    }

    @Test
    public void shouldStopWhenRemovingCyclesRecursively() {
        HashDependencyTree<Integer> dt = new HashDependencyTree<>();

        dt.insert(1, 2);
        dt.insert(2, 3);
        dt.insert(3, 1);

        dt.removeRecursively(3);
        assertTrue(dt.isEmpty());
    }
}

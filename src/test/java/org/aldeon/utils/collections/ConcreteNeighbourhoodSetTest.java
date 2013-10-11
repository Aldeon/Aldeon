package org.aldeon.utils.collections;

import org.aldeon.utils.math.Arithmetic;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConcreteNeighbourhoodSetTest {

    @Test
    public void shouldFindNearestValues() {

        ConcreteNeighbourhoodSet<Integer> set = new ConcreteNeighbourhoodSet<Integer>(new IntegerArithmetic());

        set.add(3);
        set.add(5);
        set.add(10);
        set.add(13);
        set.add(14);
        set.add(20);

        Set<Integer> result = set.closestValues(10, 3);

        assertEquals(3, result.size());
        assertTrue(result.contains(10));
        assertTrue(result.contains(13));
        assertTrue(result.contains(14));

    }

    @Test
    public void shouldReturnAllElementsWhenRequestedResultCountExceedsSetSize() {
        ConcreteNeighbourhoodSet<Integer> set = new ConcreteNeighbourhoodSet<Integer>(new IntegerArithmetic());

        set.add(3);

        Set<Integer> result = set.closestValues(5, 3);

        assertEquals(1, result.size());
        assertTrue(result.contains(3));
    }

    @Test
    public void shouldPreferGreaterWhenBothSidesAreEquallyFar() {
        ConcreteNeighbourhoodSet<Integer> set = new ConcreteNeighbourhoodSet<Integer>(new IntegerArithmetic());

        set.add(2);
        set.add(4);
        set.add(6);
        set.add(8);

        Set<Integer> result = set.closestValues(5, 1);

        assertEquals(1, result.size());
        assertTrue(result.contains(6));
    }

    private static class IntegerArithmetic implements Arithmetic<Integer> {

        @Override
        public Integer add(Integer a, Integer b) {
            return a+b;
        }

        @Override
        public Integer sub(Integer a, Integer b) {
            return a-b;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1-o2;
        }
    }
}

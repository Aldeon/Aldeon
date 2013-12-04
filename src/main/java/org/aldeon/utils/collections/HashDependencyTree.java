package org.aldeon.utils.collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HashDependencyTree<T> implements DependencyTree<T> {

    /**
     * Shows which elements are actually present in the collection
     */
    private Map<T, T> elements;

    /**
     * Parent-child relations (including unknown parents)
     */
    private SetMultimap<T, T> children;

    /**
     * Elements whose parent is not known
     */
    private Set<T> orphans;

    public HashDependencyTree() {
        elements = new HashMap<>();
        orphans = new HashSet<>();
        children = HashMultimap.create();
    }

    @Override
    public void insert(T element, T parent) {

        // 0. Check if the element already exists
        if(contains(element)) {
            return;
        }

        // 1. Check if any elements should be removed from the orphan set
        for(T child: children.get(element)) {
            orphans.remove(child);
        }

        // 2. Check if parent is already present
        if(! elements.containsKey(parent)) {
            orphans.add(element);
        }

        // 3. Put into appropriate collections
        elements.put(element, parent);
        children.put(parent, element);
    }

    @Override
    public void remove(T element) {

        T parent = elements.get(element);

        // 0. Check if the element is in the collection
        if(parent == null) {
            return;
        }

        // 1. Remove the parent-child relation
        children.remove(parent, element);

        // 2. All children of element become orphans
        Set<T> ch = children.get(element);
        if(ch != null) {
            orphans.addAll(ch);
            children.removeAll(element);
        }

        // 3. If the element as an orphan, remove it
        orphans.remove(element);

        // 4. Remove from elements
        elements.remove(element);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(T element) {
        return elements.containsKey(element);
    }

    @Override
    public Set<T> getOrphans() {
        return Collections.unmodifiableSet(orphans);
    }
}

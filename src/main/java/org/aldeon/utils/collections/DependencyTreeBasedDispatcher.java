package org.aldeon.utils.collections;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DependencyTreeBasedDispatcher<T> implements DependencyDispatcher<T> {

    private final DependencyTree<T> dependencyTree;
    private boolean stateChanged = true;
    private Set<T> returnable = new HashSet<>();
    private Set<T> returned = new HashSet<>();

    public DependencyTreeBasedDispatcher(DependencyTree<T> dependencyTree) {
        this.dependencyTree = dependencyTree;
    }

    @Override
    public T next() {

        // Try to fill returnable set (if necessary)
        if(returnable.isEmpty()) {
            if(stateChanged) {
                returnable.addAll(Sets.difference(dependencyTree.getOrphans(), returned));
                stateChanged = false;
            }
        }

        // Return one element from set
        if(returnable.isEmpty()) {
            return null;
        } else {
            Iterator<T> it = returnable.iterator();
            T element = it.next();
            it.remove();
            returned.add(element);
            return element;
        }
    }

    @Override
    public void remove(T val) {
        dependencyTree.remove(val);
        returnable.remove(val);
        returned.remove(val);
        stateChanged = true;
    }

    @Override
    public void removeRecursively(T element) {
        // 1. Remove from underlying tree
        dependencyTree.removeRecursively(element);

        // 2. Update returnable and returned sets
        returnable.clear();
        for(T orphan: dependencyTree.getOrphans()) {
            if(! returned.contains(orphan)) {
                returnable.add(orphan);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return returnable.isEmpty() && dependencyTree.isEmpty();
    }
}

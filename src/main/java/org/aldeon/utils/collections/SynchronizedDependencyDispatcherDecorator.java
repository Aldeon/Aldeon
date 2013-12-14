package org.aldeon.utils.collections;

public class SynchronizedDependencyDispatcherDecorator<T> implements DependencyDispatcher<T> {

    private final DependencyDispatcher<T> dependencyDispatcher;

    public SynchronizedDependencyDispatcherDecorator(DependencyDispatcher<T> dependencyDispatcher) {
        this.dependencyDispatcher = dependencyDispatcher;
    }

    @Override
    public synchronized T next() {
        return dependencyDispatcher.next();
    }

    @Override
    public synchronized void remove(T element) {
        dependencyDispatcher.remove(element);
    }

    @Override
    public synchronized void removeRecursively(T element) {
        dependencyDispatcher.removeRecursively(element);
    }

    @Override
    public synchronized boolean isFinished() {
        return dependencyDispatcher.isFinished();
    }
}

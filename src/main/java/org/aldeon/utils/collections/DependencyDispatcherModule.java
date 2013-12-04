package org.aldeon.utils.collections;

import com.google.inject.Binder;
import com.google.inject.Module;

import java.util.Map;

public class DependencyDispatcherModule implements Module {

    @Override
    public void configure(Binder binder) {
        // TODO: instantiate using Guice
    }

    public static <T> DependencyDispatcher<T> create(Map<T, T> values) {

        DependencyTree<T> tree = new HashDependencyTree<>();

        for(Map.Entry<T, T> pair: values.entrySet()) {
            tree.insert(pair.getKey(), pair.getValue());
        }

        return new SynchronizedDependencyDispatcherDecorator<>(new DependencyTreeBasedDispatcher<>(tree));
    }
}

package org.aldeon.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

public class MultiMapBasedEventLoop implements EventLoop {

    private SetMultimap<Class, AsyncCallback> callbacks;

    public MultiMapBasedEventLoop() {
        callbacks = HashMultimap.create();
        callbacks = Multimaps.synchronizedSetMultimap(callbacks);
    }

    @Override
    public <T extends Event> void assign(Class<T> eventType, AsyncCallback<T> callback) {
        callbacks.put(eventType, callback);
    }

    @Override
    public <T extends Event> void resign(Class<T> eventType, AsyncCallback<T> callback) {
        callbacks.remove(eventType, callback);
    }

    @Override
    public <T extends Event> void notify(final T event) {
        for(Callback c: callbacks.get(event.getClass())) {

            @SuppressWarnings("unchecked")
            Callback<T> cast = c;
            cast.call(event);
        }
    }
}

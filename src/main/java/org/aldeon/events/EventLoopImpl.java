package org.aldeon.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import org.aldeon.events.Event;
import org.aldeon.events.EventLoop;
import org.aldeon.events.Callback;


import java.util.concurrent.Executor;

public class EventLoopImpl implements EventLoop {

    private SetMultimap<Class, Pair> callbacks;

    public EventLoopImpl() {
        callbacks = HashMultimap.create();
        callbacks = Multimaps.synchronizedSetMultimap(callbacks);
    }

    @Override
    public <T extends Event> void assign(Class<T> eventType, Callback<T> callback, Executor executor) {
        callbacks.put(eventType, new Pair<>(executor, callback));
    }

    @Override
    public <T extends Event> void resign(Class<T> eventType, Callback<T> callback) {
        callbacks.remove(eventType, new Pair<>(null, callback));
    }

    @Override
    public <T extends Event> void notify(final T event) {
        for(Pair pair: callbacks.get(event.getClass())) {
            final Pair<T> castPair = pair;
            castPair.executor.execute(new Runnable() {
                @Override
                public void run() {
                    castPair.callback.call(event);
                }
            });
        }
    }

    private class Pair<T> {
        public Executor executor;
        public Callback<T> callback;

        public Pair(Executor executor, Callback<T> callback) {
            this.executor = executor;
            this.callback = callback;
        }

        @Override
        public int hashCode() {
            return callback.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Pair) && callback.equals(((Pair) obj).callback);
        }
    }
}

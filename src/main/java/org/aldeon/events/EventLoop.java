package org.aldeon.events;

import java.util.concurrent.Executor;

public interface EventLoop {
    <T extends Event> void assign(Class<T> eventType, Callback<T> callback, Executor executor);
    <T extends Event> void resign(Class<T> eventType, Callback<T> callback);
    <T extends Event> void notify(T event);
}
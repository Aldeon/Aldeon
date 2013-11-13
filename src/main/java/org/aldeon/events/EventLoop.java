package org.aldeon.events;

public interface EventLoop {
    <T extends Event> void assign(Class<T> eventType, AsyncCallback<T> callback);
    <T extends Event> void resign(Class<T> eventType, AsyncCallback<T> callback);
    <T extends Event> void notify(T event);
}

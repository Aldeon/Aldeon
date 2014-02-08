package org.aldeon.communication.nio.events;

import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Event;
import org.aldeon.events.EventLoop;
import org.aldeon.events.MultiMapBasedEventLoop;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MultiMapBasedEventLoopTest {

    @Test
    public void shouldCallOnTriggeredEvent() {

        @SuppressWarnings("unchecked")
        AsyncCallback<EventStub> callback = mock(AsyncCallback.class);

        EventLoop loop = new MultiMapBasedEventLoop();
        EventStub stub = new EventStub();

        loop.assign(EventStub.class, callback);
        loop.notify(stub);

        verify(callback).call(stub);
    }

    @Test
    public void shouldNotCallRevokedCallback() {

        @SuppressWarnings("unchecked")
        AsyncCallback<EventStub> callback = mock(AsyncCallback.class);

        EventLoop loop = new MultiMapBasedEventLoop();
        EventStub stub = new EventStub();

        loop.assign(EventStub.class, callback);
        loop.resign(EventStub.class, callback);
        loop.notify(stub);

        verify(callback, never()).call(stub);
    }

    private class EventStub implements Event {}
}

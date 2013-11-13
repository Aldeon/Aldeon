package org.aldeon.communication.nio.events;

import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Event;
import org.aldeon.events.EventLoop;
import org.aldeon.events.EventLoopImpl;
import org.aldeon.events.Callback;
import org.junit.Test;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class EventLoopImplTest {

    @Test
    public void shouldCallOnTriggeredEvent() {
        AsyncCallback<EventStub> callback = mock(AsyncCallback.class);

        EventLoop loop = new EventLoopImpl();
        EventStub stub = new EventStub();

        loop.assign(EventStub.class, callback);
        loop.notify(stub);

        verify(callback).call(stub);
    }

    @Test
    public void shouldNotCallRevokedCallback() {
        AsyncCallback<EventStub> callback = mock(AsyncCallback.class);

        EventLoop loop = new EventLoopImpl();
        EventStub stub = new EventStub();

        loop.assign(EventStub.class, callback);
        loop.resign(EventStub.class, callback);
        loop.notify(stub);

        verify(callback, never()).call(stub);
    }

    private class EventStub implements Event {}
}

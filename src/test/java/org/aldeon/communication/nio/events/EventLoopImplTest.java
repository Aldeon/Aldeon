package org.aldeon.communication.nio.events;

import org.aldeon.common.events.Event;
import org.aldeon.common.events.EventLoop;
import org.aldeon.core.EventLoopImpl;
import org.aldeon.common.events.Callback;
import org.junit.Test;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class EventLoopImplTest {

    @Test
    public void shouldCallOnTriggeredEvent() {

        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        Callback<EventStub> callback = mock(Callback.class);

        EventLoop loop = new EventLoopImpl();
        EventStub stub = new EventStub();

        loop.assign(EventStub.class, callback, executor);
        loop.notify(stub);

        verify(callback).call(stub);
    }

    @Test
    public void shouldNotCallRevokedCallback() {

        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        Callback<EventStub> callback = mock(Callback.class);

        EventLoop loop = new EventLoopImpl();
        EventStub stub = new EventStub();

        loop.assign(EventStub.class, callback, executor);
        loop.resign(EventStub.class, callback);
        loop.notify(stub);

        verify(callback, never()).call(stub);
    }

    private class EventStub implements Event {}
}

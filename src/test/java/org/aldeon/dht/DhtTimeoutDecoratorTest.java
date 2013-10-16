package org.aldeon.dht;

import org.aldeon.common.model.Identifier;
import org.aldeon.common.net.TemporaryPeerAddress;
import org.aldeon.utils.time.TimeProvider;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DhtTimeoutDecoratorTest {

    @Test
    public void shouldAddElementIfTimeoutDidNotOccur() {

        Dht<Stub> dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);

        when(timerMock.getTime()).thenReturn((long) 10);

        DhtTimeoutDecorator<Stub> dht = new DhtTimeoutDecorator<>(dhtMock, timerMock);
        Stub stub = new Stub(20);
        dht.insert(stub);

        verify(dhtMock).insert(stub);
    }

    @Test
    public void shouldNotAddElementIfTimeoutDidOccur() {

        Dht<Stub> dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);

        when(timerMock.getTime()).thenReturn((long) 10);

        DhtTimeoutDecorator<Stub> dht = new DhtTimeoutDecorator<>(dhtMock, timerMock);
        Stub stub = new Stub(5);
        dht.insert(stub);

        verify(dhtMock, never()).insert(stub);
    }

    @Test
    public void shouldRemoveElement() {

        Dht<Stub> dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);

        when(timerMock.getTime()).thenReturn((long) 10);

        DhtTimeoutDecorator<Stub> dht = new DhtTimeoutDecorator<>(dhtMock, timerMock);
        Stub stub = new Stub(5);
        dht.remove(stub);

        verify(dhtMock).remove(stub);
    }

    @Test
    public void shouldNotRemoveElementBeforeTimeout() {

        Dht<Stub> dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);

        when(timerMock.getTime()).thenReturn((long) 0);

        DhtTimeoutDecorator<Stub> dht = new DhtTimeoutDecorator<>(dhtMock, timerMock);
        Stub stub = new Stub(5);
        dht.insert(stub);
        dht.refresh();

        verify(dhtMock, never()).remove(stub);
    }

    @Test
    public void shouldRemoveElementAfterTimeout() {

        Dht<Stub> dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);

        when(timerMock.getTime()).thenReturn((long) 0);

        DhtTimeoutDecorator<Stub> dht = new DhtTimeoutDecorator<>(dhtMock, timerMock);
        Stub stub = new Stub(5);
        dht.insert(stub);

        when(timerMock.getTime()).thenReturn((long) 10);

        dht.refresh();

        verify(dhtMock).remove(stub);
    }

    @Test
    public void shouldAskUnderlyingDhtForNearestValues() {

        Dht<Stub> dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);
        Set<Stub> setMock = mock(Set.class);
        Identifier idMock = mock(Identifier.class);
        int maxValues = 42;

        when(timerMock.getTime()).thenReturn((long) 0);
        when(dhtMock.getNearest(idMock, maxValues)).thenReturn(setMock);

        DhtTimeoutDecorator<Stub> dht = new DhtTimeoutDecorator<>(dhtMock, timerMock);

        assertEquals(setMock, dht.getNearest(idMock, maxValues));
    }


    private class Stub implements TemporaryPeerAddress {

        private final long timeout;

        public Stub(long timeout) {
            this.timeout = timeout;
        }

        @Override
        public long getTimeout() {
            return timeout;
        }

        @Override
        public Identifier getIdentifier() {
            return null;
        }
    }
}

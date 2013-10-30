package org.aldeon.dht;

import org.aldeon.common.dht.Dht;
import org.aldeon.common.model.Identifier;
import org.aldeon.common.net.address.TemporaryIdentifiablePeerAddress;
import org.aldeon.utils.time.TimeProvider;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DhtTimeoutDecoratorTest {

    @Test
    public void shouldAddElementIfTimeoutDidNotOccur() {
        Dht dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);
        TemporaryIdentifiablePeerAddress addressMock = mock(TemporaryIdentifiablePeerAddress.class);

        when(timerMock.getTime()).thenReturn((long) 10);
        when(addressMock.getTimeout()).thenReturn((long) 20);

        DhtTimeoutDecorator dht = new DhtTimeoutDecorator(dhtMock, timerMock);
        dht.insert(addressMock);

        verify(dhtMock).insert(addressMock);
    }

    @Test
    public void shouldNotAddElementIfTimeoutDidOccur() {
        Dht dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);
        TemporaryIdentifiablePeerAddress addressMock = mock(TemporaryIdentifiablePeerAddress.class);

        when(timerMock.getTime()).thenReturn((long) 10);
        when(addressMock.getTimeout()).thenReturn((long) 5);

        DhtTimeoutDecorator dht = new DhtTimeoutDecorator(dhtMock, timerMock);
        dht.insert(addressMock);

        verify(dhtMock, never()).insert(addressMock);
    }

    @Test
    public void shouldRemoveElement() {
        Dht dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);
        TemporaryIdentifiablePeerAddress addressMock = mock(TemporaryIdentifiablePeerAddress.class);

        when(timerMock.getTime()).thenReturn((long) 10);
        when(addressMock.getTimeout()).thenReturn((long) 5);

        DhtTimeoutDecorator dht = new DhtTimeoutDecorator(dhtMock, timerMock);
        dht.remove(addressMock);

        verify(dhtMock).remove(addressMock);
    }

    @Test
    public void shouldNotRemoveElementBeforeTimeout() {
        Dht dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);
        TemporaryIdentifiablePeerAddress addressMock = mock(TemporaryIdentifiablePeerAddress.class);

        when(timerMock.getTime()).thenReturn((long) 0);
        when(addressMock.getTimeout()).thenReturn((long) 5);

        DhtTimeoutDecorator dht = new DhtTimeoutDecorator(dhtMock, timerMock);
        dht.insert(addressMock);
        dht.refresh();

        verify(dhtMock, never()).remove(addressMock);
    }

    @Test
    public void shouldRemoveElementAfterTimeout() {
        Dht dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);
        TemporaryIdentifiablePeerAddress addressMock = mock(TemporaryIdentifiablePeerAddress.class);

        when(timerMock.getTime()).thenReturn((long) 0);
        when(addressMock.getTimeout()).thenReturn((long) 5);

        DhtTimeoutDecorator dht = new DhtTimeoutDecorator(dhtMock, timerMock);
        dht.insert(addressMock);

        when(timerMock.getTime()).thenReturn((long) 10);

        dht.refresh();

        verify(dhtMock).remove(addressMock);
    }

    @Test
    public void shouldAskUnderlyingDhtForNearestValues() {
        Dht dhtMock = mock(Dht.class);
        TimeProvider timerMock = mock(TimeProvider.class);
        Set setMock = mock(Set.class);
        Identifier idMock = mock(Identifier.class);
        int maxValues = 42;

        when(timerMock.getTime()).thenReturn((long) 0);
        when(dhtMock.getNearest(idMock, maxValues)).thenReturn(setMock);

        DhtTimeoutDecorator dht = new DhtTimeoutDecorator(dhtMock, timerMock);

        assertEquals(setMock, dht.getNearest(idMock, maxValues));
    }
}

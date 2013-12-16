package org.aldeon.dht2;

import org.aldeon.dht2.interest.orders.Order;
import org.aldeon.dht2.interest.orders.TopicOrderLine;
import org.aldeon.dht2.interest.orders.TopicOrderLineImpl;
import org.aldeon.events.Callback;
import org.aldeon.networking.common.PeerAddress;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TopicOrderLineImplTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSatisfyOrderWhenAddressInsertedFirst() {

        TopicOrderLine line = new TopicOrderLineImpl();

        PeerAddress address = mock(PeerAddress.class);
        Order order = mock(Order.class);
        Callback callback = mock(Callback.class);

        when(order.callback()).thenReturn(callback);

        line.addAddress(address);
        line.addOrder(order);

        verify(callback).call(address);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSatisfyOrderWhenOrderInsertedFirst() {

        TopicOrderLine line = new TopicOrderLineImpl();

        PeerAddress address = mock(PeerAddress.class);
        Order order = mock(Order.class);
        Callback callback = mock(Callback.class);

        when(order.callback()).thenReturn(callback);

        line.addOrder(order);
        line.addAddress(address);

        verify(callback).call(address);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSatisfyOneOrderAtATime() {

        TopicOrderLine line = new TopicOrderLineImpl();

        PeerAddress address = mock(PeerAddress.class);
        Order order1 = mock(Order.class);
        Callback callback1 = mock(Callback.class);
        Order order2 = mock(Order.class);
        Callback callback2 = mock(Callback.class);

        when(order1.callback()).thenReturn(callback1);
        when(order2.callback()).thenReturn(callback2);

        line.addAddress(address);
        line.addOrder(order1);
        line.addOrder(order2);

        verify(callback2, never()).call(address);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSatisfyNextOrderWhenFirstIsRevoked() {

        TopicOrderLine line = new TopicOrderLineImpl();

        PeerAddress address = mock(PeerAddress.class);
        Order order1 = mock(Order.class);
        Callback callback1 = mock(Callback.class);
        Order order2 = mock(Order.class);
        Callback callback2 = mock(Callback.class);

        when(order1.callback()).thenReturn(callback1);
        when(order2.callback()).thenReturn(callback2);

        line.addAddress(address);
        line.addOrder(order1);
        line.addOrder(order2);

        line.delOrder(order1);

        verify(callback2).call(address);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotSatisfyNextOrderWhenAddressIsDeleted() {

        TopicOrderLine line = new TopicOrderLineImpl();

        PeerAddress address = mock(PeerAddress.class);
        Order order1 = mock(Order.class);
        Callback callback1 = mock(Callback.class);
        Order order2 = mock(Order.class);
        Callback callback2 = mock(Callback.class);

        when(order1.callback()).thenReturn(callback1);
        when(order2.callback()).thenReturn(callback2);

        line.addAddress(address);
        line.addOrder(order1);
        line.addOrder(order2);

        line.delAddress(address);
        line.delOrder(order1);

        verify(callback2, never()).call(address);
    }





}

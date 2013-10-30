package org.aldeon.dht;

import org.aldeon.common.dht.InterestTracker;
import org.aldeon.common.model.Identifier;
import org.aldeon.common.net.address.PeerAddress;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class InterestTrackerImplTest {

    @Test
    public void shouldFetchIfPeerAddressDoesMatch() {

        InterestTracker tracker = new InterestTrackerImpl();

        PeerAddress addressMock = mock(PeerAddress.class);
        Identifier identifierMock = mock(Identifier.class);

        tracker.add(addressMock, identifierMock);

        Collection result = tracker.getInterestedPeers(identifierMock);

        assertTrue(result.contains(addressMock));
    }

    @Test
    public void shouldNotFetchIfPeerAddressDoesNotMatch() {
        InterestTracker tracker = new InterestTrackerImpl();

        PeerAddress addressMock = mock(PeerAddress.class);
        Identifier identifierMock = mock(Identifier.class);
        Identifier otherIdentifierMock = mock(Identifier.class);

        tracker.add(addressMock, identifierMock);

        Collection result = tracker.getInterestedPeers(otherIdentifierMock);

        assertFalse(result.contains(addressMock));
    }
}

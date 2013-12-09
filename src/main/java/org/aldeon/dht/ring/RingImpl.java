package org.aldeon.dht.ring;

import com.google.inject.Inject;
import org.aldeon.model.Identifiable;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.utils.collections.ConcreteNeighbourhoodSet;
import org.aldeon.utils.collections.NeighbourhoodSet;
import org.aldeon.utils.helpers.ByteBuffers;
import org.aldeon.utils.various.Arithmetic;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Class implementing the Ring interface. The closeness of PeerAddresses
 * is defined as the difference of their corresponding ByteBuffers,
 * interpreted as byte-stored integers of arbitrary size.
 *
 * This class should be rewritten to accept NeighbourhoodSet as its
 * dependency.
 *
 */
public class RingImpl implements Ring {

    private NeighbourhoodSet<Identifiable> circle;

    @Inject
    public RingImpl() {
        circle = new ConcreteNeighbourhoodSet<>(new IdentifiableArithmetic());
    }

    @Override
    public void insert(PeerAddress address) {
        circle.add(address);
    }

    @Override
    public void remove(PeerAddress address) {
        circle.remove(address);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<PeerAddress> getNearest(Identifier identifier, int maxResults) {
        Set<PeerAddress> results = new HashSet<>();

        for(Identifiable peer: circle.closestValues(new IdentifiableStub(identifier), maxResults)) {
            results.add((PeerAddress) peer);
        }

        return results;
    }

    private class IdentifiableStub implements Identifiable {

        private Identifier identifier;

        public IdentifiableStub(Identifier identifier) {
            this.identifier = identifier;
        }

        @Override
        public Identifier getIdentifier() {
            return identifier;
        }
    }

    private class IdentifiableArithmetic implements Arithmetic<Identifiable> {

        @Override
        public Identifiable add(Identifiable a, Identifiable b) {
            ByteBuffer result = ByteBuffers.add(a.getIdentifier().getByteBuffer(), b.getIdentifier().getByteBuffer());
            return new IdentifiableStub(Identifier.fromByteBuffer(result, false));
        }

        @Override
        public Identifiable sub(Identifiable a, Identifiable b) {
            ByteBuffer result = ByteBuffers.add(a.getIdentifier().getByteBuffer(), b.getIdentifier().getByteBuffer());
            return new IdentifiableStub(Identifier.fromByteBuffer(result, false));
        }

        @Override
        public int compare(Identifiable o1, Identifiable o2) {
            return ByteBuffers.compare(o1.getIdentifier().getByteBuffer(), o2.getIdentifier().getByteBuffer());
        }
    }
}

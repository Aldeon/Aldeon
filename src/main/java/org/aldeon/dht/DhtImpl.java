package org.aldeon.dht;

import org.aldeon.common.model.Identifiable;
import org.aldeon.common.model.Identifier;
import org.aldeon.common.net.address.IdentifiablePeerAddress;
import org.aldeon.utils.collections.ConcreteNeighbourhoodSet;
import org.aldeon.utils.collections.NeighbourhoodSet;
import org.aldeon.utils.math.Arithmetic;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Class implementing the Dht interface. The closeness of PeerAddresses
 * is defined as the difference of their corresponding ByteBuffers,
 * interpreted as byte-stored integers of arbitrary size.
 *
 * This class should be rewritten to accept NeighbourhoodSet as its
 * dependency.
 *
 * @param <T>
 */
public class DhtImpl<T extends IdentifiablePeerAddress> implements Dht<T> {

    private NeighbourhoodSet<Identifiable> circle;
    private Arithmetic<ByteBuffer> arithmetic;

    public DhtImpl(Arithmetic<ByteBuffer> arithmetic) {
        this.arithmetic = arithmetic;
        circle = new ConcreteNeighbourhoodSet<>(new IdentifiableArithmetic());
    }

    @Override
    public void insert(T address) {
        circle.add(address);
    }

    @Override
    public void remove(T address) {
        circle.remove(address);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<T> getNearest(Identifier identifier, int maxResults) {
        Set<T> results = new HashSet<>();

        for(Identifiable peer: circle.closestValues(new IdentifiableStub(identifier), maxResults)) {
            results.add((T) peer);
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

    private class IdentifierStub implements Identifier {

        private ByteBuffer buffer;

        public IdentifierStub(ByteBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public ByteBuffer getByteBuffer() {
            return buffer;
        }
    }

    private class IdentifiableArithmetic implements Arithmetic<Identifiable> {

        @Override
        public Identifiable add(Identifiable a, Identifiable b) {
            ByteBuffer result = arithmetic.add(a.getIdentifier().getByteBuffer(), b.getIdentifier().getByteBuffer());
            return new IdentifiableStub(new IdentifierStub(result));
        }

        @Override
        public Identifiable sub(Identifiable a, Identifiable b) {
            ByteBuffer result = arithmetic.add(a.getIdentifier().getByteBuffer(), b.getIdentifier().getByteBuffer());
            return new IdentifiableStub(new IdentifierStub(result));
        }

        @Override
        public int compare(Identifiable o1, Identifiable o2) {
            return arithmetic.compare(o1.getIdentifier().getByteBuffer(), o2.getIdentifier().getByteBuffer());
        }
    }
}

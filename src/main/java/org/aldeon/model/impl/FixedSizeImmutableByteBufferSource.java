package org.aldeon.model.impl;

import org.aldeon.model.ByteSource;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.base64.Base64Module;

import java.nio.ByteBuffer;

/**
 *  Holds an immutable ByteBuffer of a specified size.
 */
public class FixedSizeImmutableByteBufferSource implements ByteSource {

    private static final Codec CODEC = new Base64Module().get();

    private ByteBuffer buffer;


    /**
     * Registers a ByteBuffer with specified parameters
     * @param buffer source buffer
     * @param shouldCopy indicates if the source buffer should be duplicated before being registered.
     * @param expectedLength defines how long the buffer should be.
     * @throws IllegalArgumentException
     */
    public FixedSizeImmutableByteBufferSource(ByteBuffer buffer, boolean shouldCopy, int expectedLength) throws IllegalArgumentException{

        if(buffer.capacity() == expectedLength) {
            buffer.clear();
            if(shouldCopy) {
                this.buffer = ByteBuffer.allocate(expectedLength);
                this.buffer.put(buffer);
            } else {
                this.buffer = buffer;
            }
            this.buffer.clear();
        } else {
            throw new IllegalArgumentException("Buffer capacity must equal " + expectedLength + " bytes.");
        }
    }

    @Override
    public final ByteBuffer getByteBuffer() {
        return buffer.asReadOnlyBuffer();
    }

    @Override
    public String toString() {
        return CODEC.encode(getByteBuffer());
    }
}

package org.aldeon.model;

import java.nio.ByteBuffer;

public class FixedSizeByteBufferSource implements ByteSource {
    private ByteBuffer buffer;

    public FixedSizeByteBufferSource(ByteBuffer buffer, boolean shouldCopy, int expectedLength) throws IllegalArgumentException{

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
    public ByteBuffer getByteBuffer() {
        ByteBuffer readOnly = buffer.asReadOnlyBuffer();
        readOnly.clear();
        return readOnly;
    }
}

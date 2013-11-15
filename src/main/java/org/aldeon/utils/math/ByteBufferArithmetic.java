package org.aldeon.utils.math;

import org.aldeon.utils.helpers.ByteBuffers;

import java.nio.ByteBuffer;

public class ByteBufferArithmetic implements Arithmetic<ByteBuffer> {

    @Override
    public ByteBuffer add(ByteBuffer a, ByteBuffer b) {
        return ByteBuffers.add(a, b);
    }

    @Override
    public ByteBuffer sub(ByteBuffer a, ByteBuffer b) {
        return ByteBuffers.sub(a, b);
    }

    @Override
    public int compare(ByteBuffer a, ByteBuffer b) {
        return ByteBuffers.compare(a, b);
    }
}

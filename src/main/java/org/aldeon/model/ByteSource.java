package org.aldeon.model;

import java.nio.ByteBuffer;

/**
 * Interface for classes representable in form of a byte array
 */
public interface ByteSource {
    public ByteBuffer getByteBuffer();
}

package org.aldeon.model;

/**
 * Interface for the identifier classes. Each message and peer has a unique identifier.
 */
public interface Identifier extends ByteSource{
    public static final int LENGTH_BITS = 256;
    public static final int LENGTH_BYTES = LENGTH_BITS / 8;
}

package org.aldeon.common.model;

/**
 * Interface for the identifier classes. Each message and peer has a unique identifier.
 */
public interface Identifier extends ByteSource{
    public static final int LENGTH = 256;
}

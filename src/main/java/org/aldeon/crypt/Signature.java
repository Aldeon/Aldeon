package org.aldeon.crypt;

import org.aldeon.model.ByteSource;

public interface Signature extends ByteSource {
    public static int LENGTH_BITS = 1024;
    public static int LENGTH_BYTES = LENGTH_BITS / 8;
}

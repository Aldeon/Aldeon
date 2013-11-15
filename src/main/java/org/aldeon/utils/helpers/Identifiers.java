package org.aldeon.utils.helpers;

import org.aldeon.model.Identifier;

import java.nio.ByteBuffer;
import java.util.Random;

public class Identifiers {

    private static final Random rand;

    static {
        rand = new Random();
    }

    @Deprecated
    public static Identifier random() {
        ByteBuffer buf = ByteBuffer.allocate(Identifier.LENGTH_BYTES);
        rand.nextBytes(buf.array());
        return Identifier.fromByteBuffer(buf, false);
    }
}

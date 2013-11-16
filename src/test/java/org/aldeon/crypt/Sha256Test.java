package org.aldeon.crypt;

import org.aldeon.utils.helpers.ByteBuffers;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertTrue;

public class Sha256Test {

    public static final String TEST_STRING = "test string";
    public static final String OTHER_STRING = "other string";
    public static final String TEST_HASH = "d5579c46dfcc7f18207013e65b44e4cb4e2c2298f4ac457ba8f82743f31e930b";

    @Test
    public void shouldCalculateCorrectHash() {

        ByteBuffer expected = ByteBuffers.fromHex(TEST_HASH);

        Sha256 sha = new Sha256();

        sha.add(TEST_STRING.getBytes());
        ByteBuffer result = sha.calculate();

        assertTrue(ByteBuffers.equal(expected, result));
    }

    @Test
    public void shouldClearMemoryBetweenConcesutiveUses() {

        ByteBuffer expected = ByteBuffers.fromHex(TEST_HASH);

        Sha256 sha = new Sha256();

        sha.add(OTHER_STRING.getBytes());
        sha.calculate();

        sha.add(TEST_STRING.getBytes());
        ByteBuffer result = sha.calculate();

        assertTrue(ByteBuffers.equal(expected, result));
    }

}

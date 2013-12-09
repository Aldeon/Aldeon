package org.aldeon.utils.helpers;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class ByteBuffersTest {

    @Test
    public void shouldSubProperly() {

        ByteBuffer a = ByteBuffer.allocate(2);
        // 26247    --->    0110 0110 1000 0111
        //   102    --->    0110 0110
        //   135    --->              1000 0111

        a.put((byte) 102);
        a.put((byte) 135);
        a.clear();

        ByteBuffer b = ByteBuffer.allocate(2);
        //  8124    --->    0001 1111 1011 1100
        //    31    --->    0001 1111
        //   188    --->              1011 1100

        b.put((byte) 31);
        b.put((byte) 188);
        b.clear();

        ByteBuffer c = ByteBuffers.sub(a, b);
        // 18123    --->    0100 0110 1100 1011     // 26247 - 8124 = 18123
        //    70    --->    0100 0110
        //   203    --->              1100 1011

        assertEquals(0, c.position());
        assertEquals(2, c.capacity());
        assertEquals((byte)70,  c.get(0));
        assertEquals((byte)203, c.get(1));
    }

    @Test
    public void shouldSubAndIgnoreRemainder() {

        ByteBuffer a = ByteBuffer.allocate(2);
        // 12598    --->    0011 0001 0011 0110
        //    49    --->    0011 0001
        //    54    --->              0011 0110

        a.put((byte) 49);
        a.put((byte) 54);
        a.clear();

        ByteBuffer b = ByteBuffer.allocate(2);
        // 45681    --->    1011 0010 0111 0001
        //   178    --->    1011 0010
        //   113    --->              0111 0001

        b.put((byte) 178);
        b.put((byte) 113);
        b.clear();

        ByteBuffer c = ByteBuffers.sub(a, b);
        // 32453    --->    0111 1110 1100 0101     // 12598 - 45681 = -33083, -33083 + 2^16 = 32453
        //   126    --->    0111 1110
        //   197    --->              1100 0101

        assertEquals(0, c.position());
        assertEquals(2, c.capacity());
        assertEquals((byte)126, c.get(0));
        assertEquals((byte)197, c.get(1));
    }

    @Test
    public void shouldAdd() {
        ByteBuffer a = ByteBuffer.allocate(2);

        a.put((byte) 127);
        a.put((byte) 255);
        a.clear();

        ByteBuffer b = ByteBuffer.allocate(2);
        b.put((byte) 127);
        b.put((byte) 255);
        b.clear();

        ByteBuffer c = ByteBuffers.add(a, b);

        assertEquals(0, c.position());
        assertEquals(2, c.capacity());
        assertEquals((byte)255, c.get(0));
        assertEquals((byte)254, c.get(1));
    }

    @Test
    public void shouldAddAndIgnoreRemainder() {
        ByteBuffer a = ByteBuffer.allocate(2);
        a.put((byte) 127);
        a.put((byte) 255);
        a.clear();

        ByteBuffer b = ByteBuffer.allocate(2);
        b.put((byte) 128);
        b.put((byte) 255);
        b.clear();

        ByteBuffer c = ByteBuffers.add(a, b);

        assertEquals(0, c.position());
        assertEquals(2, c.capacity());
        assertEquals((byte)0,   c.get(0));
        assertEquals((byte)254, c.get(1));
    }

    @Test
    public void shouldAddNumbersOfDifferentSize() {
        ByteBuffer a = ByteBuffer.allocate(3);
        a.put((byte) 0);
        a.put((byte) 127);
        a.put((byte) 255);
        a.clear();

        ByteBuffer b = ByteBuffer.allocate(2);
        b.put((byte) 128);
        b.put((byte) 255);
        b.clear();

        ByteBuffer c = ByteBuffers.add(a, b);

        assertEquals(0, c.position());
        assertEquals(3, c.capacity());
        assertEquals((byte)1,   c.get(0));
        assertEquals((byte)0,   c.get(1));
        assertEquals((byte)254, c.get(2));
    }
}

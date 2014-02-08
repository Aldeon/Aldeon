package org.aldeon.utils.codec.hex;


import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class HexCodecTest {

    @Test
    public void shouldProperlyEncode() {

        Codec hex = new HexCodec();

        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.put((byte) 0x4f);
        buf.put((byte) 0xf2);
        buf.put((byte) 0x07);
        buf.put((byte) 0x18);

        String encoded = hex.encode(buf);

        assertEquals("4FF20718", encoded);
    }

    @Test
    public void shouldProperlyDecode() throws ConversionException {

        Codec hex = new HexCodec();

        String encoded = "5F41EA";

        ByteBuffer buf = hex.decode(encoded);

        assertEquals(3, buf.capacity());
        assertEquals((byte) 0x5f, buf.get(0));
        assertEquals((byte) 0x41, buf.get(1));
        assertEquals((byte) 0xea, buf.get(2));
    }

}

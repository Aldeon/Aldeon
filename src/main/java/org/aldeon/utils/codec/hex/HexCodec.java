package org.aldeon.utils.codec.hex;


import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.helpers.ByteBuffers;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;

public class HexCodec implements Codec {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    //StackOverflow: My own tiny benchmarks (a million bytes a thousand times, 256 bytes 10 million times)
    // showed it to be much faster than any other alternative, about half the time on long arrays.
    // Compared to the answer I took it from, switching to bitwise ops --- as suggested in the discussion ---
    // cut about 20% off of the time for long arrays.
    // (Edit: When I say it's faster than the alternatives, I mean the alternative code offered in the discussions.
    // Performance is equivalent to Commons Codec, which uses very similar code.)

    @Override
    public String encode(ByteBuffer buffer) {

        byte[] bytes = ByteBuffers.array(buffer);
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public ByteBuffer decode(String string) throws ConversionException {
        return ByteBuffer.wrap(DatatypeConverter.parseHexBinary(string));
    }
}

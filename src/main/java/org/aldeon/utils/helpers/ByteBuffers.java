package org.aldeon.utils.helpers;

import javax.xml.bind.DatatypeConverter;
import java.nio.*;

public class ByteBuffers {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private ByteBuffers() { }

    public static boolean equal(ByteBuffer a, ByteBuffer b) {
        return a.equals(b);
    }

    public static ByteBuffer xor(ByteBuffer a, ByteBuffer b) {

        // Better safe than sorry
        a = a.asReadOnlyBuffer();
        b = b.asReadOnlyBuffer();

        ByteBuffer c = ByteBuffer.allocate(a.remaining());

        while(a.hasRemaining()) {
            int vc = 0xFF & (a.get() ^ b.get());
            c.put((byte) vc);
        }

        c.flip();
        return c;
    }

    public static ByteBuffer sub(ByteBuffer a, ByteBuffer b) {

        // Better safe than sorry
        a = a.asReadOnlyBuffer();
        b = b.asReadOnlyBuffer();

        int n = Math.max(a.remaining(), b.remaining());

        ByteBuffer c = ByteBuffer.allocate(n);

        int sub, rem = 0;

        for(int i = n - 1; i >= 0; --i) {

            sub = padGet(a, i, n) - padGet(b, i, n) - rem;

            if(sub < 0) {
                sub += 256;
                rem = 1;
            } else {
                rem = 0;
            }

            c.put(i, toByte(sub));
        }
        c.clear();
        return c;
    }

    public static ByteBuffer add(ByteBuffer a, ByteBuffer b) {

        // Better safe than sorry
        a = a.asReadOnlyBuffer();
        b = b.asReadOnlyBuffer();

        int n = Math.max(a.remaining(), b.remaining());

        ByteBuffer c = ByteBuffer.allocate(n);

        int sum, rem = 0;

        for(int i = n - 1; i >= 0; --i) {
            sum = rem + padGet(a, i, n) + padGet(b, i, n);
            if(sum >= 256) {
                rem = 1;
                sum -= 256;
            } else {
                rem = 0;
            }
            c.put(i, toByte(sum));
        }
        c.clear();
        return c;
    }

    public static int compare(ByteBuffer a, ByteBuffer b) {

        int n = Math.max(a.capacity(), b.capacity());

        a = a.asReadOnlyBuffer();
        b = b.asReadOnlyBuffer();

        int sub;
        for(int i=0; i<n; ++i) {
            sub = padGet(a, i, n) - padGet(b, i, n);
            if(sub > 0) return 1;
            if(sub < 0) return -1;
        }
        return 0;
    }

    private static byte toByte(int i) {
        return (i >= 128)
                ? (byte) (i - 256)
                : (byte) i;
    }

    private static int toInt(byte b) {
        return (b < 0)
                ? (int) b + 256
                : (int) b;
    }

    private static int padGet(ByteBuffer buf, int pos, int pad) {
        pad -= buf.remaining();
        return pos < pad
                ? 0
                : toInt(buf.get(buf.position() + pos - pad));
    }

    public static ByteBuffer fromHex(String hex) {
        return ByteBuffer.wrap(DatatypeConverter.parseHexBinary(hex));
    }

    //StackOverflow: My own tiny benchmarks (a million bytes a thousand times, 256 bytes 10 million times)
    // showed it to be much faster than any other alternative, about half the time on long arrays.
    // Compared to the answer I took it from, switching to bitwise ops --- as suggested in the discussion ---
    // cut about 20% off of the time for long arrays.
    // (Edit: When I say it's faster than the alternatives, I mean the alternative code offered in the discussions.
    // Performance is equivalent to Commons Codec, which uses very similar code.)
    public static String toHex(ByteBuffer byteBuffer) {
        byteBuffer = clone(byteBuffer);
        byte[] bytes = byteBuffer.array();

        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static ByteBuffer clone(ByteBuffer buffer) {
        ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity());
        buffer.rewind();
        newBuffer.put(buffer);
        buffer.rewind();
        newBuffer.flip();
        return newBuffer;
    }
}

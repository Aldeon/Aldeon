package org.aldeon.utils.helpers;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;

public class ByteBuffers {

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
}

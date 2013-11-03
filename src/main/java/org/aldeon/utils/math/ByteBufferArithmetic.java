package org.aldeon.utils.math;

import java.nio.ByteBuffer;

public class ByteBufferArithmetic implements Arithmetic<ByteBuffer> {

    @Override
    public ByteBuffer xor(ByteBuffer a, ByteBuffer b) {
        int n = Math.max(a.capacity(), b.capacity());

        ByteBufferReader aa = new ByteBufferReader(a, n);
        ByteBufferReader bb = new ByteBufferReader(b, n);

        ByteBuffer c = ByteBuffer.allocate(n);

        int xor;

        for(int i = n-1; i >=0; --i) {
            xor = aa.get(i) ^ bb.get(i);
            c.put(i, toByte(xor));
        }
        c.clear();
        return c;
    }

    @Override
    public ByteBuffer add(ByteBuffer a, ByteBuffer b) {
        int n = Math.max(a.capacity(), b.capacity());

        ByteBufferReader aa = new ByteBufferReader(a, n);
        ByteBufferReader bb = new ByteBufferReader(b, n);

        ByteBuffer c = ByteBuffer.allocate(n);

        int sum, rem = 0;

        for(int i = n-1; i >=0; --i) {
            sum = aa.get(i) + bb.get(i) + rem;
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

    @Override
    public ByteBuffer sub(ByteBuffer a, ByteBuffer b) {
        int n = Math.max(a.capacity(), b.capacity());

        ByteBufferReader aa = new ByteBufferReader(a, n);
        ByteBufferReader bb = new ByteBufferReader(b, n);

        ByteBuffer c = ByteBuffer.allocate(n);

        int sub, va, vb, rem = 0;

        for(int i = n-1; i >= 0; --i) {
            va = aa.get(i);
            vb = bb.get(i);
            sub = va - vb - rem;
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

    @Override
    public int compare(ByteBuffer a, ByteBuffer b) {
        int n = Math.max(a.capacity(), b.capacity());
        ByteBufferReader aa = new ByteBufferReader(a, n);
        ByteBufferReader bb = new ByteBufferReader(b, n);
        int sub;
        for(int i=0; i<n; ++i) {
            sub = aa.get(i) - bb.get(i);
            if(sub > 0) return 1;
            if(sub < 0) return -1;
        }
        return 0;
    }

    private static class ByteBufferReader {
        private int pad;
        private ByteBuffer buffer;

        public ByteBufferReader(ByteBuffer buffer, int targetLength) {
            pad = targetLength - buffer.capacity();
            this.buffer = buffer;
            if(pad < 0) throw new IllegalArgumentException();
        }

        public int get(int i) {
            return (i < pad)
                    ? 0
                    : toInt(buffer.get(i - pad));
        }


    }
}

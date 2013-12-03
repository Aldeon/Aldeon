package org.aldeon.utils.helpers;

import org.aldeon.model.ByteSource;

import java.nio.ByteBuffer;

public class BufPrint {

    public static String hex(ByteSource bs) {
        return hex(bs.getByteBuffer());
    }

    public static String hex(byte[] bytes) {
        return hex(ByteBuffer.wrap(bytes));
    }

    public static String hex(ByteBuffer buf) {

        int cap = buf.capacity();
        int pos = buf.position();
        int lim = buf.limit();
        int rem = buf.remaining();
        boolean readOnly = buf.isReadOnly();
        boolean hasArray = buf.hasArray();
        boolean isDirect = buf.isDirect();

        buf = buf.asReadOnlyBuffer();

        StringBuilder b = new StringBuilder();



        b.append("         +-------------------------------------------------+\n");
        b.append("         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |\n");
        b.append("+--------+-------------------------------------------------+\n");

        int lineNum = 0;

        while(buf.hasRemaining()) {

            b.append('|');
            b.append(padLeft(Integer.toHexString(lineNum), '0', 8));
            b.append('|');

            for(int i = 0; i < 16; ++i) {
                if(buf.hasRemaining()) {
                    b.append(' ');
                    b.append(byteToHex(buf.get()));
                } else {
                    b.append("   ");
                }
            }
            b.append(" |\n");

            lineNum += 16;
        }

        b.append("+--------+-------------------------------------------------+\n");
        b.append("         | Capacity  : " + padRight("" + cap, ' ', 10) + "Read only : " + padRight("" + readOnly, ' ', 14) + "|\n");
        b.append("         | Position  : " + padRight("" + pos, ' ', 10) + "Has array : " + padRight("" + hasArray, ' ', 14) + "|\n");
        b.append("         | Limit     : " + padRight("" + lim, ' ', 10) + "Is direct : " + padRight("" + isDirect, ' ', 14) + "|\n");
        b.append("         | Remaining : " + padRight("" + rem, ' ', 36) + "|\n");
        b.append("         +-------------------------------------------------+\n");


        return b.toString();
    }

    public static String dec(ByteBuffer buf) {

        int cap = buf.capacity();
        int pos = buf.position();
        int lim = buf.limit();
        int rem = buf.remaining();
        boolean readOnly = buf.isReadOnly();
        boolean hasArray = buf.hasArray();
        boolean isDirect = buf.isDirect();

        buf = buf.asReadOnlyBuffer();

        StringBuilder b = new StringBuilder();


        b.append("         +-----------------------------------------------------------------+\n");
        b.append("         |  0   1   2   3   4   5   6   7   8   9   a   b   c   d   e   f  |\n");
        b.append("+--------+-----------------------------------------------------------------+\n");

        int lineNum = 0;

        while(buf.hasRemaining()) {

            b.append('|');
            b.append(padLeft(Integer.toHexString(lineNum), '0', 8));
            b.append('|');

            for(int i = 0; i < 16; ++i) {
                if(buf.hasRemaining()) {
                    b.append(' ');

                    b.append(padLeft(byteToDec(buf.get()), ' ', 3));
                } else {
                    b.append("   ");
                }
            }
            b.append(" |\n");

            lineNum += 16;
        }

        b.append("+--------+-------------------------------------------------+---------------+\n");
        b.append("         | Capacity  : " + padRight("" + cap, ' ', 10) + "Read only : " + padRight("" + readOnly, ' ', 14) + "|\n");
        b.append("         | Position  : " + padRight("" + pos, ' ', 10) + "Has array : " + padRight("" + hasArray, ' ', 14) + "|\n");
        b.append("         | Limit     : " + padRight("" + lim, ' ', 10) + "Is direct : " + padRight("" + isDirect, ' ', 14) + "|\n");
        b.append("         | Remaining : " + padRight("" + rem, ' ', 36) + "|\n");
        b.append("         +-------------------------------------------------+\n");


        return b.toString();
    }

    public static String bin(ByteBuffer buf) {

        int cap = buf.capacity();
        int pos = buf.position();
        int lim = buf.limit();
        int rem = buf.remaining();
        boolean readOnly = buf.isReadOnly();
        boolean hasArray = buf.hasArray();
        boolean isDirect = buf.isDirect();

        buf = buf.asReadOnlyBuffer();

        StringBuilder b = new StringBuilder();


        b.append("         +-------------------------------------------------------------------------------------------------------------------------------------------------+\n");
        b.append("         |  0        1        2        3        4        5        6        7        8        9        a        b        c        d        e        f       |\n");
        b.append("+--------+-------------------------------------------------------------------------------------------------------------------------------------------------+\n");

        int lineNum = 0;

        while(buf.hasRemaining()) {

            b.append('|');
            b.append(padLeft(Integer.toHexString(lineNum), '0', 8));
            b.append('|');

            for(int i = 0; i < 16; ++i) {
                if(buf.hasRemaining()) {
                    b.append(' ');
                    b.append(byteToBin(buf.get()));
                } else {
                    b.append("   ");
                }
            }
            b.append(" |\n");

            lineNum += 16;
        }

        b.append("+--------+-------------------------------------------------+-----------------------------------------------------------------------------------------------+\n");
        b.append("         | Capacity  : " + padRight("" + cap, ' ', 10) + "Read only : " + padRight("" + readOnly, ' ', 14) + "|\n");
        b.append("         | Position  : " + padRight("" + pos, ' ', 10) + "Has array : " + padRight("" + hasArray, ' ', 14) + "|\n");
        b.append("         | Limit     : " + padRight("" + lim, ' ', 10) + "Is direct : " + padRight("" + isDirect, ' ', 14) + "|\n");
        b.append("         | Remaining : " + padRight("" + rem, ' ', 36) + "|\n");
        b.append("         +-------------------------------------------------+\n");


        return b.toString();
    }

    public static String byteToHex(byte b) {
        return padLeft(Integer.toHexString(b & 0xFF), '0', 2);
    }

    public static String byteToBin(byte b) {
        String ret = "";
        int v = (b<0) ? b+256 : b;
        for(int i=0;i<8;++i) {
            int rem = v % 2;
            ret = rem + ret;
            v = (v-rem) / 2;
        }
        return ret;
    }

    public static String byteToDec(byte b) {
        int v = (b<0) ? b+256 : b;
        return "" + v;
    }

    private static String padLeft(String s, char pad, int size) {
        while(s.length() < size)
            s = pad + s;
        return s;
    }

    private static String padRight(String s, char pad, int size) {
        while(s.length() < size)
            s += pad ;
        return s;
    }
}

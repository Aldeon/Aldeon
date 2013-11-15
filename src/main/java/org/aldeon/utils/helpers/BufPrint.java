package org.aldeon.utils.helpers;

import java.nio.ByteBuffer;

public class BufPrint {

    /*
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+
|00000000| 48 54 54 50 2f 31 2e 31 20 32 30 30 20 4f 4b 0d |
|00000010| 0a 43 6f 6e 74 65 6e 74 2d 54 79 70 65 3a 20 74 |
|00000020| 65 78 74 2f 6a 73 6f 6e 3b 20 63 68 61 72 73 65 |
|00000030| 74 3d 55 54 46 2d 38 0d 0a 0d 0a                |
+--------+-------------------------------------------------+

     */

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


        //        |00000000| 252 193  55  24  32  63  42 170 249 195 237 249  73  49 215 206 |
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



        //                 | 00000111 11100100 10111111 10000011 00100100 11010110 11000110 10101000 01011111 10000011 10000010 10000011 11010101 00001111 11101110 00000101 |
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

package org.aldeon.crypt;


import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static ByteBuffer getHashFromByteBuffer(ByteBuffer data) {
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        }catch(NoSuchAlgorithmException e){

        }finally {
            return ByteBuffer.wrap(sha256.digest(data.array()));
        }
    }
    public static String getHexStringFromHash(ByteBuffer hash){
        byte hashBytes[]=hash.array();
        StringBuilder hexString = new StringBuilder();
        for (int i: hashBytes) {
            hexString.append(Integer.toHexString(0XFF & i));
        }
        return new String(hexString);
    }
}

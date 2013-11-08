package org.aldeon.crypt;

import org.aldeon.model.ByteSource;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;



public class RsaKey implements ByteSource {

    Key key;
    ByteBuffer byteKey;
    SecureRandom seed;

    public ByteBuffer encrypt(ByteBuffer buffer) {
        try {
            Cipher cipher=Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, seed);
            byte[] encrypted = cipher.doFinal(buffer.array());
            return ByteBuffer.wrap(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ByteBuffer decrypt(ByteBuffer buffer) {
        try {
            Cipher cipher=Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, seed);
            byte[] decrypted = cipher.doFinal(buffer.array());
            return ByteBuffer.wrap(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ByteBuffer getByteBuffer() {
        return byteKey;
    }

    public Key getKey(){
        return key;
    }

    public RsaKey(Key k, SecureRandom s){
        key=k;
        byteKey=ByteBuffer.wrap(k.getEncoded());
        seed=s;
    }

    public RsaKey(Key k){
        key=k;
        byteKey=ByteBuffer.wrap(k.getEncoded());
        try {
            seed=SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
package org.aldeon.crypt;

import org.aldeon.model.ByteSource;
import org.aldeon.model.FixedSizeImmutableByteBufferSource;
import java.nio.ByteBuffer;
import java.security.Signature;
import java.security.PublicKey;
import java.security.PrivateKey;


public class RsaSignature extends FixedSizeImmutableByteBufferSource implements org.aldeon.crypt.Signature {

    public RsaSignature(ByteBuffer buffer, boolean shouldCopy, int expectedLength) throws IllegalArgumentException {
        super(buffer, shouldCopy, expectedLength);
    }

    public ByteBuffer signData(ByteBuffer data, PrivateKey key){
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(key);
            signature.update(data.array());
            return ByteBuffer.wrap(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verifySignature(ByteBuffer dataToVerify, ByteBuffer originalData, PublicKey key){
        try{
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(key);
            signature.update(originalData.array());
            return signature.verify(dataToVerify.array());
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}

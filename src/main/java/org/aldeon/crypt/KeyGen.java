package org.aldeon.crypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;

public class KeyGen {
    static{
        Security.addProvider(new BouncyCastleProvider());
    }


    public static KeyPair generateKeyPair(SecureRandom seed){
        try{
            KeyPairGenerator keyGen= KeyPairGenerator.getInstance("RSA","BC");
            keyGen.initialize(1024,seed);           //Length of RSA key
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static KeyPair generateKeyPair(){
        try{
            SecureRandom seed = SecureRandom.getInstance("SHA1PRNG");
            KeyPairGenerator keyGen= KeyPairGenerator.getInstance("RSA","BC");
            keyGen.initialize(1024,seed);           //Length of RSA key
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}

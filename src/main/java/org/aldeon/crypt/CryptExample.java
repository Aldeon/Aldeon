package org.aldeon.crypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Created with IntelliJ IDEA.
 * User: Prophet
 * Date: 08.11.13
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public class CryptExample {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
    //////////////////////////////////////////////////////////////
    ////////////////////RSA EXAMPLE///////////////////////////////
    SecureRandom s = new SecureRandom();
    s.setSeed(s.generateSeed(128));
    KeyPair keys = KeyGen.generateKeyPair(new SecureRandom());
    RsaKey publicKey = new RsaKey(keys.getPublic(),s);
    RsaKey privateKey = new RsaKey(keys.getPrivate(),s);
    ByteBuffer test;
    test=publicKey.encrypt(ByteBuffer.wrap("ASDASDASDZXCZXCZXCZXCZXCZXCZASQWDZVSRGXCV".getBytes()));
    System.out.print("ENCRYPTED: ");
    System.out.println(new String(test.array()));
    test=privateKey.decrypt(test);
    System.out.println("public->private PLAINTEXT: "+new String(test.array()));

    test=privateKey.encrypt(ByteBuffer.wrap("QWEQWEQWEQWEQWEQWEQWE".getBytes()));
    System.out.print("ENCRYPTED: ");
    System.out.println(new String(test.array()));
    test=publicKey.decrypt(test);
    System.out.println("private->public PLAINTEXT: "+new String(test.array()));

    RsaSignature sig = new RsaSignature(ByteBuffer.allocate(Signature.LENGTH_BYTES),false,Signature.LENGTH_BYTES);
    ByteBuffer test2=sig.signData(test,(PrivateKey)privateKey.getKey());
    System.out.println("Weryfikacja podpisu:    " + sig.verifySignature(test2, test, (PublicKey) publicKey.getKey()));
    System.out.println("\n\n---------------KEY RETRIEVAL TESTS---------------\n\n");
    try {
        RsaPrivateKey recoveredPrivate = new RsaPrivateKey(privateKey.getByteBuffer());
        RsaPublicKey recoveredPublic = new RsaPublicKey(publicKey.getByteBuffer());

        test=recoveredPublic.encrypt(ByteBuffer.wrap("ASDASDASDZXCZXCZXCZXCZXCZXCZASQWDZVSRGXCV".getBytes()));
        System.out.print("ENCRYPTED: ");
        System.out.println(new String(test.array()));
        test=recoveredPrivate.decrypt(test);
        System.out.println("public->private PLAINTEXT: "+new String(test.array()));

        test=recoveredPrivate.encrypt(ByteBuffer.wrap("QWEQWEQWEQWEQWEQWEQWE".getBytes()));
        System.out.print("ENCRYPTED: ");
        System.out.println(new String(test.array()));
        test=recoveredPublic.decrypt(test);
        System.out.println("private->public PLAINTEXT: "+new String(test.array()));

        sig = new RsaSignature(ByteBuffer.allocate(Signature.LENGTH_BYTES),false,Signature.LENGTH_BYTES);
        test2=sig.signData(test,(PrivateKey)recoveredPrivate.getKey());
        System.out.println("Weryfikacja podpisu:    " + sig.verifySignature(test2, test, (PublicKey) recoveredPublic.getKey()));
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    ///////////////////////////////////////////
    ///////////////HASH EXAMPLE////////////////
    System.out.println("--------------TESTING HASH FUNCTION------------------");
    ByteBuffer asd  = Hash.getHashFromByteBuffer(ByteBuffer.wrap("asdasd".getBytes()));
    System.out.println(Hash.getHexStringFromHash(asd));
    // This should actually be called in GUI
    System.out.println("Press any key to close...");
    System.in.read();
}
}

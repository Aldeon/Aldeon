package org.aldeon.utils.helpers;

import org.aldeon.crypt.Key;
import org.aldeon.crypt.Hash;
import org.aldeon.crypt.signer.Sha256;
import org.aldeon.crypt.Signer;
import org.aldeon.crypt.signer.SignerModule;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.model.Signature;
import org.aldeon.model.exception.CorruptedMessageException;

import java.nio.ByteBuffer;

public class Messages {

    private static final Signer signer;

    static {
        signer = new SignerModule().get();
    }

    /**
     * Creates a new message and signs it with a given pair of keys
     * @param parent parent message identifier. null means a new topic
     * @param pubKey public key
     * @param privKey private key
     * @param content message content
     * @return signed message
     */
    public static Message createAndSign(Identifier parent, Key pubKey, Key privKey, String content) {

        if(
                pubKey == null
                || pubKey.getType() != Key.Type.PUBLIC
                || privKey == null
                || privKey.getType() != Key.Type.PRIVATE
                || content == null)
        {
            throw new IllegalArgumentException();
        }

        if(parent == null)
            parent = Identifier.empty();

        signer.add(parent);
        signer.add(pubKey);
        signer.add(content.getBytes());

        Signature sig = signer.sign(privKey);

        Hash hash = new Sha256();
        hash.add(sig);
        Identifier id = Identifier.fromByteBuffer(hash.calculate(), false);

        return createUnsafe(id, parent, pubKey, content, sig);
    }

    /**
     * Creates a new message and performs all necessary security checks
     * @param identifier
     * @param parent
     * @param pubKey
     * @param content
     * @param sig
     * @return
     */
    public static Message create(Identifier identifier, Identifier parent, Key pubKey, String content, Signature sig) throws CorruptedMessageException {

        Message msg = createUnsafe(identifier, parent, pubKey, content, sig);

        if(verify(msg)) {
            return msg;
        } else {
            throw new CorruptedMessageException();
        }
    }

    /**
     * Creates the new message and ignores all the security checks
     * @param identifier
     * @param parent
     * @param pubKey
     * @param content
     * @param sig
     * @return
     */
    public static Message createUnsafe(Identifier identifier, Identifier parent, Key pubKey, String content, Signature sig) {

        if(
                identifier == null
                || parent == null
                || pubKey == null
                || pubKey.getType() != Key.Type.PUBLIC
                || content == null
                || sig == null)
        {
            throw new IllegalArgumentException();
        }

        return new MessageImpl(identifier, parent, pubKey, content, sig);
    }

    /**
     * Verifies that the message is signed properly
     * @param m message to verify
     * @return
     */
    public static boolean verify(Message m) {
        if(m.getAuthorPublicKey().getType() != Key.Type.PUBLIC) return false;

        signer.add(m.getParentMessageIdentifier());
        signer.add(m.getAuthorPublicKey());
        signer.add(m.getContent().getBytes());

        // Check the signature
        if(signer.verify(m.getAuthorPublicKey(), m.getSignature())) {
            // Check if the identifier comes from the signature
            Signature sig = m.getSignature();
            Hash hash = new Sha256();
            hash.add(sig);
            ByteBuffer idBuf = hash.calculate();

            return ByteBuffers.equal(idBuf, m.getIdentifier().getByteBuffer());
        }
        return false;
    }

    private static class MessageImpl implements Message {
        private final Identifier msgIdentifier;
        private final Key authorKey;
        private final Identifier parentIdentifier;
        private final Signature signature;
        private final String content;

        public MessageImpl(Identifier id, Identifier parentId, Key authorKey, String content, Signature sig) {
            this.msgIdentifier = id;
            this.authorKey = authorKey;
            this.parentIdentifier = parentId;
            this.signature = sig;
            this.content = content;
        }

        @Override
        public Key getAuthorPublicKey() {
            return authorKey;
        }

        @Override
        public Identifier getParentMessageIdentifier() {
            return parentIdentifier;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public Signature getSignature() {
            return signature;
        }

        @Override
        public Identifier getIdentifier() {
            return msgIdentifier;
        }

        @Override
        public String toString() {
            return      "ID      : " + getIdentifier() + "\n"
                    +   "Parent  : " + getParentMessageIdentifier() + "\n"
                    +   "Author  : " + getAuthorPublicKey() + "\n"
                    +   "Sig     : " + getSignature() + "\n"
                    +   "Content : " + getContent();
        }
    }

}

package org.aldeon.model;

import org.aldeon.crypt.Signature;

/**
 * User message. Contains every information needed to determine
 * its position in a tree and verify the identity of its author.
 */
public interface Message extends Identifiable{

    /**
     * Identifier of the author of the message.
     * @return
     */
    Identifier getAuthorIdentifier();

    /**
     * Identifier of the parent message. In other words, the
     * message that this message refers to.
     * @return
     */
    Identifier getParentMessageIdentifier();

    /**
     * Actual content of the message. Should be encoded in UTF-8.
     * @return
     */

    String getContent();

    /**
     * Proves that the getAuthorIdentifier() points to a real author.
     * @return
     */
    Signature getSignature();
}
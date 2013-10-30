package org.aldeon.model;

import org.aldeon.crypt.Signature;

public interface Message extends Identifiable{
    Identifier getAuthorIdentifier();
    Identifier getParentMessageIdentifier();
    String getContent();
    Signature getSignature();
}

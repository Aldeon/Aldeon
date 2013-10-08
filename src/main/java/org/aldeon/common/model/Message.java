package org.aldeon.common.model;

public interface Message extends Identifiable{
    Identifier getAuthorIdentifier();
    String getContent();
    Identifier getPArentMessageIdentifier();
    Signature getSignature();
}

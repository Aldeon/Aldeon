package org.aldeon.model;

import org.aldeon.crypt.Signature;

public class MessageImpl implements Message {
    private final Identifier msgIdentifier;
    private final Identifier authorIdentifier;
    private final Identifier parentIdentifier;
    private final Signature signature;
    private final String content;

    public MessageImpl(Identifier id, Identifier authorId, Identifier parentId, Signature sig, String content) {
        this.msgIdentifier = id;
        this.authorIdentifier = authorId;
        this.parentIdentifier = parentId;
        this.signature = sig;
        this.content = content;
    }

    @Override
    public Identifier getAuthorIdentifier() {
        return authorIdentifier;
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
    public Identifier getMsgIdentifier() {
        return msgIdentifier;
    }
}
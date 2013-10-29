package org.aldeon.common.model;

import java.util.Collection;

public interface Storage {
    Message getMessageByIdentifier(Identifier identifier);
    void putMessage(Message message);
    void dropMessage(Identifier identifier);

    Identifier getMessageIdentifierByXor(Identifier xor);
    Collection<Identifier> getMessagesByParent(Identifier parent);
}

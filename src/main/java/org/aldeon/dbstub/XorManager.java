package org.aldeon.dbstub;

import org.aldeon.db.exception.IdentifierAlreadyPresentException;
import org.aldeon.db.exception.UnknownIdentifierException;
import org.aldeon.db.exception.UnknownParentException;
import org.aldeon.model.Identifier;

import java.util.Set;

public interface XorManager {

    public void putId(Identifier id, Identifier parent) throws UnknownParentException, IdentifierAlreadyPresentException;

    public void delId(Identifier id) throws UnknownIdentifierException;
    public Identifier getXor(Identifier id) throws UnknownIdentifierException;
    public Set<Identifier> getIds(Identifier xor);
    public Set<Identifier> getChildren(Identifier parent);
    public boolean contains(Identifier id);
}

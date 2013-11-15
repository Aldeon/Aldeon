package org.aldeon.dbstub;

import org.aldeon.db.exception.IdentifierAlreadyPresentException;
import org.aldeon.db.exception.InconsistentDatabaseStateException;
import org.aldeon.db.exception.UnknownIdentifierException;
import org.aldeon.db.exception.UnknownParentException;
import org.aldeon.model.Identifier;
import org.aldeon.utils.collections.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XorManager {

    // P - parent, Q - xor
    private Map<Identifier, Pair<Identifier, Identifier>> data;


    public XorManager() {
        data = new HashMap<>();
    }

    public void putId(Identifier id, Identifier parent) throws UnknownParentException, IdentifierAlreadyPresentException {
        if(data.get(parent) == null && !parent.isEmpty()) {
            throw new UnknownParentException();
        } else {
            if(data.get(id) == null && !id.isEmpty()) {
                data.put(id, new Pair<>(parent, id));
                try {
                    update(parent, id);
                } catch (UnknownParentException e) {
                    throw new InconsistentDatabaseStateException("Database contains conflicting information", e);
                }
            } else {
                throw new IdentifierAlreadyPresentException();
            }
        }
    }

    public void delId(Identifier id) throws UnknownIdentifierException {
        Pair<Identifier, Identifier> pair = data.get(id);
        if(pair == null) {
            throw new UnknownIdentifierException();
        } else {
            recDelChildren(id);
            data.remove(id);
            try {
                update(pair.getP(), pair.getQ());
            } catch (UnknownParentException e) {
                throw new InconsistentDatabaseStateException("Database contains conflicting information", e);
            }
        }
    }

    public Identifier getXor(Identifier id) throws UnknownIdentifierException {
        Pair<Identifier, Identifier> pair = data.get(id);
        if(pair == null) {
            throw new UnknownIdentifierException();
        } else {
            return pair.getQ();
        }
    }

    public Set<Identifier> getIds(Identifier xor) {

        Set<Identifier> result = new HashSet<>();

        for(Map.Entry<Identifier, Pair<Identifier, Identifier>> entry: data.entrySet()) {
            if(entry.getValue().getQ().equals(xor)) {
                result.add(entry.getKey());
            }
        }

        return result;
    }

    private void update(Identifier id, Identifier xor) throws UnknownParentException {
        while(!id.isEmpty()) {
            Pair<Identifier, Identifier> p = data.get(id);
            if(p == null) {
                throw new UnknownParentException();
            } else {
                p.setQ(p.getQ().xor(xor));
                id = p.getP();
            }
        }
    }

    private void recDelChildren(Identifier id) {
        for(Map.Entry<Identifier, Pair<Identifier, Identifier>> entry: data.entrySet()) {
            Pair<Identifier, Identifier> p = entry.getValue();
            if(id.equals(p.getP())){
                recDelChildren(entry.getKey());
                data.remove(entry.getKey());
            }
        }
    }
}

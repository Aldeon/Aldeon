package org.aldeon.model;


/**
 * Interface for classes that can be uniquely identified by and Identifier class (message, peers)
 */
public interface Identifiable {
    public Identifier getIdentifier();
}

package org.aldeon.model;

import org.aldeon.crypt.Key;

/**
 * User - an author of messages.
 */
public interface User {
    Key getPublicKey();
    String getName();
}

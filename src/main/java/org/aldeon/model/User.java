package org.aldeon.model;

import org.aldeon.crypt.Key;

public interface User extends Identifiable {
    Key getPublicKey();
}
